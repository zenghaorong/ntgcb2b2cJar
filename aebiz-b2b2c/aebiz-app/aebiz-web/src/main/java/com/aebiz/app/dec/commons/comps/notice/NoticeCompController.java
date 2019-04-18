package com.aebiz.app.dec.commons.comps.notice;

import com.aebiz.app.dec.commons.service.ContentForCompService;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.comps.notice.vo.NoticeCompModel;
import com.aebiz.app.dec.commons.comps.notice.vo.NoticeContentModel;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.content.ContentDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yewei on 2017/5/26.
 */
@Controller
@RequestMapping("/noticeComp")
public class NoticeCompController extends BaseCompController {
    @Autowired
    public ContentForCompService contentForCompService;
    private String contextPath ="";
    @Override
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {
        Document doc = Jsoup.parse(pageViewHtml);
        String compId = bcm.getCompId();
        Element compEle = doc.getElementById(compId);
        NoticeCompModel ncm = (NoticeCompModel) bcm;
        contextPath= (String) wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
        //渲染标题栏
        Element categoryNameEle = compEle.getElementById(compId+"_categoryName");
        categoryNameEle.html(ncm.getCategoryName());
        Element categoryUrlEle = compEle.getElementById(compId+"_categoryUrl");
        categoryUrlEle.attr("href",ncm.getCategoryUrl()+ncm.getCategoryId());
        //渲染公告列表
        Element loopEle = compEle.getElementById(compId+"_loop");
        renderList(loopEle,ncm);
        super.delCompLoading(doc,ncm);
        return doc.html();
    }

    @RequestMapping("/ajaxLoadData")
    @SJson
    public Map ajaxLoadData(HttpServletRequest request){
        Map resultMap = new HashMap();
        List dataList = new ArrayList();
        String contentCateUuid = request.getParameter("contentCateUuid");
        List<ContentDTO> contentList = contentForCompService.getContListByCateId(contentCateUuid);
        if(contentList!=null&&contentList.size()>0) {
            for (ContentDTO content : contentList) {
                dataList.add(castToNoticeContentModel(content));
            }
        }
        resultMap.put("data",dataList);
        return resultMap;
    }

    @RequestMapping("/toParamsDesign")
    @Override
    public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
        List categoryList = contentForCompService.getContentCategory();
        String toUrl = (String) request.getAttribute("toParamsJspURL");
        request.setAttribute("categoryList",categoryList);
        return toUrl;

    }
    @Override
    public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
        NoticeCompModel ccm = (NoticeCompModel) bcm;
        designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(ccm));
        designJs = designJs.replaceAll("\\$_compId", ccm.getCompId());
        return designJs;
    }
    /**
     * 渲染公告列表
     * @param loopEle
     * @param ncm
     */
    private void renderList(Element loopEle, NoticeCompModel ncm){
        Element liEle = loopEle.select("li").first();
        List<ContentDTO> contentList = contentForCompService.getContListByCateId(ncm.getCategoryId());
        for(ContentDTO content:contentList){
            Element newEle = liEle.clone();
            Element contentUrlEle = newEle.select(".j_contentUrl").first();
//            Element noteEle = newEle.select(".j_note").first();
            Element titleEle = newEle.select(".j_title").first();
            NoticeContentModel m = castToNoticeContentModel(content);
            contentUrlEle.attr("href",contextPath+ncm.getContentUrl()+"&contentUuid="+m.getContentId());
//            noteEle.html(m.getNote());
            titleEle.html(m.getTitle());
            loopEle.appendChild(newEle);
        }
        liEle.remove();
    }

    /**
     * 转换Map至model
     * @return
     */
    private NoticeContentModel castToNoticeContentModel(ContentDTO content){
        NoticeContentModel model = new NoticeContentModel();
        model.setContentId(content.getArticlUuid());
        model.setNote(content.getArticlValue());
        model.setTitle(content.getArticlTitle());
        return model;
    }
}
