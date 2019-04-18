package com.aebiz.app.dec.commons.comps.bottomHelpCenter;

import com.aebiz.app.cms.modules.models.Cms_channel;
import com.aebiz.app.cms.modules.services.CmsChannelService;
import com.aebiz.app.dec.commons.service.ContentForCompService;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.comps.bottomHelpCenter.vo.BottomHelpCenterCompModel;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.content.ContentCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.content.ContentDTO;
import com.aebiz.app.dec.commons.utils.CommonUtil;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by yewei on 2017/5/9.
 */
@Controller
@RequestMapping("/bottomHelpCenterComp")
public class BottomHelpCenterCompController extends BaseCompController {
    @Autowired
    private ContentForCompService contentForCompService;

    @Autowired
    private CmsChannelService cmsChannelService;
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm){
        Document doc = Jsoup.parse(pageViewHtml);
        super.delCompLoading(doc, bcm);
        BottomHelpCenterCompModel bhcm = (BottomHelpCenterCompModel)bcm;
        //调用接口返回帮助中心数据
        Set<Map.Entry<ContentCategoryDTO, List<ContentDTO>>> set = getBottomHelpCenterInfo(bhcm.getChannelId());
        //初始化页面帮助中心数据
        Element element = doc.select("#"+bhcm.getCompId()+"_cotcont").first();
        String contextPath = Strings.sNull(wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH));
        intiPageViewHtml(element,bhcm,set,contextPath);

        return doc.html();
    }

    /**
     * 异步加载返回页面数据
     * @return
     */
    @RequestMapping(value="/ajaxLoadData")
    @SJson
    public Object ajaxLoadData(@RequestParam("channelId") String channelId, HttpServletRequest request, HttpServletResponse response){
        NutMap map=new NutMap();
        if(Strings.isEmpty(channelId)){
            map.put("channel",null);
            return Json.toJson(map);
        }else{
            Set<Map.Entry<ContentCategoryDTO, List<ContentDTO>>> set = getBottomHelpCenterInfo(channelId);
            Iterator<Map.Entry<ContentCategoryDTO, List<ContentDTO>>> iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry<ContentCategoryDTO, List<ContentDTO>> entery = iterator.next();
                ContentCategoryDTO cat = entery.getKey();
                List<ContentDTO> contList = entery.getValue();
                map.put(Json.toJson(cat), contList);
            }
            return Json.toJson(map);
        }
    }

    /**
     * 跳转到参数设置页面
     */
    @RequestMapping(value="toParamsDesign")
    public String toParamsDesign(HttpServletResponse response, HttpServletRequest request){
        List<Cms_channel> channels=cmsChannelService.query(Cnd.where("parentId","=",""));
        request.setAttribute("channels",channels);
        String toUrl = Strings.sNull(request.getAttribute("toParamsJspURL"));
        return toUrl;
    }

    /**
     * 替换js
     */
    public String genJs(String designJs,WebPageModel wpm,BaseCompModel bcm){
        designJs = designJs.replaceAll("\\$_compId",bcm.getCompId());
        designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(bcm));
        return designJs;
    }


    /**
     * 初始化页面html
     * @param element
     */
    private void intiPageViewHtml(Element element,BottomHelpCenterCompModel bhcm,Set<Map.Entry<ContentCategoryDTO, List<ContentDTO>>> set,String contextPath){
        Element clone = element.children().first().clone();
        String contentUrl = bhcm.getContentUrl();
        String channelId=bhcm.getChannelId();
        if(set != null && set.size()>0){
            Iterator<Map.Entry<ContentCategoryDTO,List<ContentDTO>>> iterator = set.iterator();
            while(iterator.hasNext()){
                Map.Entry<ContentCategoryDTO, List<ContentDTO>>  entery = iterator.next();
                ContentCategoryDTO cat = entery.getKey();

                clone.select("img").attr("src", cat.getIconPath());
                Element firstdd = clone.select("dd").first().clone();
                firstdd.html(cat.getCategoryName());
                clone.select("dd").remove();
                clone.append(firstdd.outerHtml());

                List<ContentDTO> contList = entery.getValue();
                if(contList!=null && contList.size()>0){
                    for(ContentDTO m : contList){
                        clone.appendElement("dd").appendElement("a").attr("target","_blank").attr("href", CommonUtil.getContentUrl(contextPath,contentUrl,m.getArticlUuid())).html(m.getArticlTitle());
                    }
                }
                element.append(clone.outerHtml());
            }
        }
        element.children().first().remove();
    }

    /**
     * 调用接口返回底部帮助中心文章分类及分类下内容
     * @return
     */
    private Set<Map.Entry<ContentCategoryDTO, List<ContentDTO>>> getBottomHelpCenterInfo(String channelId){
        Map<ContentCategoryDTO, List<ContentDTO>> jsonMap = contentForCompService.getContentCategoryByUuid(channelId);
        return  jsonMap.entrySet();
    }

}
