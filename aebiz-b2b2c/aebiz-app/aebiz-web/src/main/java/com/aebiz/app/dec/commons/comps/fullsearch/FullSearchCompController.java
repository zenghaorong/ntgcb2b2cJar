package com.aebiz.app.dec.commons.comps.fullsearch;


import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.comps.fullsearch.vo.FullSearchCompModel;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.dec.commons.utils.CommonUtil;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yewei on 2017/6/2.
 */
@Controller
@RequestMapping("/fullSearchComp")
public class FullSearchCompController extends BaseCompController {
    @Autowired
    public ProductForCompService productForCompService;

    private String contextPath ="";

    @Override
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {
        Document doc = Jsoup.parse(pageViewHtml);
        String compId = bcm.getCompId();
        Element compEle = doc.getElementById(compId);
        FullSearchCompModel fcm = (FullSearchCompModel) bcm;
        contextPath= (String) wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
        //渲染热词
        renderHot(compEle,fcm);
        super.delCompLoading(doc,fcm);
        return doc.html();
    }

    @RequestMapping("/queryRelatedWords")
    @SJson
    public List queryRelatedWords(@RequestParam("keyword")String keyword){
        Map param = new HashMap();
        param.put("key",keyword);
        return productForCompService.getLikeKey(keyword);
    }

    @RequestMapping("/toParamsDesign")
    @Override
    public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
        String toUrl = (String) request.getAttribute("toParamsJspURL");
        return toUrl;

    }

    @Override
    public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
        FullSearchCompModel fcm = (FullSearchCompModel)bcm;
        designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(fcm));
        designJs = designJs.replaceAll("\\$_compId", fcm.getCompId());
        return designJs;
    }

    @RequestMapping("/ajaxLoadData")
    @ResponseBody
    public List ajaxLoadData(@RequestParam("compId") String compId, HttpServletRequest request) {
        return null;
    }

    /**
     * 渲染热词
     * @param compEle
     * @param fcm
     */
    private void renderHot(Element compEle, FullSearchCompModel fcm){
        int isHaveHot = fcm.getIsHaveHot();
        int defaultSelect = fcm.getDefaultSelect();
        Element loopEle = compEle.getElementById(fcm.getCompId() + "_hotwords");
        Element typeStoreEle = compEle.getElementById(fcm.getCompId() + "_typeStore");
        Element typeValueEle = compEle.getElementById(fcm.getCompId()+"_typeValue");
        if(isHaveHot == fcm.HAVE_HOT) {
            String[] hotwords = fcm.getHotWords().split(",");
            //TODO 调用接口获取热词
            /*String dataJson = productForCompService.getHotKey(new HashMap<String, Object>());
            Map dataMap = JSONObject.parseObject(dataJson);*/
            Element hotwordAEle = loopEle.select(".j_hotword").first();
            if(hotwords!=null) {
                for (int i = 0; i < hotwords.length; ++i) {
                    String hotword = hotwords[i];
                    Element newEle = hotwordAEle.clone();
                    newEle.html(hotword);
                    newEle.attr("href", CommonUtil.getFullPath(contextPath, fcm.getProductSearchUrl(), "keyword",hotword));
                    loopEle.appendChild(newEle);
                }
            }
            hotwordAEle.remove();
        }else{
            loopEle.remove();
        }
    }
}
