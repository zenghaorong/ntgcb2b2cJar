package com.aebiz.app.dec.commons.comps.storesearch;

import com.aebiz.app.dec.commons.comps.storesearch.vo.StoreSearchCompModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.CompProductDetailDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductDetailDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 金辉 on 2016/12/21.
 */
@Controller
@RequestMapping("/storeSearchComp")
public class StoreSearchCompController extends BaseCompController {

    @Autowired
    private ProductForCompService productForCompService;

    @Override
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {
        Document doc = Jsoup.parse(pageViewHtml);
        super.delCompLoading(doc,bcm);
        return doc.html();
    }

    @RequestMapping("/toParamsDesign")
    public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
        String toUrl = (String) request.getAttribute("toParamsJspURL");
        return toUrl;
    }

    @RequestMapping("/ajaxLoadData")
    @ResponseBody
    public String ajaxLoadData(@RequestParam("compId")String compId, HttpServletRequest request){
       return null;
    }

    @Override
    public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
        StoreSearchCompModel scm = (StoreSearchCompModel) bcm;
        designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(scm));
        designJs = designJs.replaceAll("\\$_compId", scm.getCompId());
        return designJs;
    }
    @RequestMapping("/getProductStoreId")
    @ResponseBody
    public String getProductStoreId(@RequestParam("sku")String sku){
        CompProductDetailDTO reslutMap = productForCompService.getProductDetailInfo(sku);
        ProductDetailDTO pmm = reslutMap.getProductDetailDTO();
        return pmm.getStoreUuid();
    }
}
