package com.aebiz.app.dec.commons.comps.cartcomp;


import com.aebiz.app.dec.commons.comps.cartcomp.vo.CartCompModel;
import com.aebiz.app.dec.commons.service.PlatInfoForCompService;
import com.aebiz.app.dec.commons.service.ShoppingCartService;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.member.modules.models.Member_cart;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatImageCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.cart.CartProductDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.commons.utils.MoneyUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 购物车组件
 * Created by 金辉 on 2016/12/12.
 */
@Controller
@RequestMapping("/cartComp")
public class CartCompController extends BaseCompController {
    @Autowired
    public ShoppingCartService shoppingCartService;
    @Autowired
    private PlatInfoForCompService platInfoForCompService;
    private String contextPath = "";

    @Override
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

        //读取模板页面
        Document doc = Jsoup.parse(pageViewHtml);
        //获取组件模块element
        Element compEle = doc.getElementById(bcm.getCompId());
        CartCompModel ccm = (CartCompModel) bcm;

        //获取接口数据
        contextPath = (String) wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
        Map dataMap = shoppingCartService.getCart();
        //加载上方页面
        this.loadUpFrame(compEle, ccm, dataMap);

        //加载下方悬浮框
        loadDropFrame(compEle, ccm.getCompId(), dataMap, ccm);

        if (ccm.getIsEasyMode() == 0) {
            //控制下方悬浮框显示
            Element element = compEle.getElementById(ccm.getCompId() + "_drop");
            element.remove();
            super.delCompLoading(doc, ccm);
            return doc.html();
        }

        super.delCompLoading(doc, ccm);
        return doc.html();
    }

    @RequestMapping("/toParamsDesign")
    @Override
    public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
        String toUrl = (String) request.getAttribute("toParamsJspURL");
        Map<String, Object> map = new HashMap<>();
        map.put("parentUuid", "");
        List<PlatImageCategoryDTO> imageLibCategoryList = platInfoForCompService.getImageLibCategoryById();
        request.setAttribute("list", imageLibCategoryList);
        return toUrl;
    }

    @Override
    public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
        CartCompModel ccm = (CartCompModel) bcm;
        designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(ccm));
        designJs = designJs.replaceAll("\\$_compId", ccm.getCompId());
        return designJs;
    }

    @RequestMapping("/ajaxLoadData")
    @ResponseBody
    public String ajaxLoadData() {
        Map data = shoppingCartService.getCart();
        return Json.toJson(data);
    }

    @RequestMapping("/deletePro")
    @ResponseBody
    public String deletePro(@RequestParam("attrId") String attrId,
                            HttpServletRequest request, HttpServletResponse response) {
        Map dataMap = shoppingCartService.removeCart(response,request, attrId, request.getCookies());
        return Json.toJson(dataMap);
    }

    /**
     * 构造下方悬浮框
     *
     * @param compEle
     * @param compId
     * @return
     */
    private void loadDropFrame(Element compEle, String compId, Map cartModel, CartCompModel ccm) {
        //商品总数
        Element numEle = compEle.getElementById(compId + "_num");
        List<Member_cart> carts=(List<Member_cart>)cartModel.get("cartProduct");
        //商品总价
        Element priceEle = compEle.getElementById(compId + "_price");
        Element productEle = compEle.getElementById(compId + "_product");
        Element liEle = productEle.select(".j_productModel").first();
        int totalPrice=0;
        int num=0;
        if (!Lang.isEmpty(carts)) {
            for(Member_cart cart:carts){
                totalPrice +=(cart.getPrice())*(cart.getNum());
                num +=cart.getNum();
                Element newLi = initProductInfo(liEle, cart, ccm);
                productEle.appendChild(newLi);
            }
            numEle.html(String.valueOf(num));
            liEle.remove();
            priceEle.html(String.valueOf(MoneyUtil.fenToYuan(totalPrice)));
        } else {
            priceEle.html("0");
            numEle.html("0");
            liEle.remove();
        }
    }

    /**
     * 构造商品信息数据
     *
     * @param modelEle 商品信息模板element
     * @return
     */
    private Element initProductInfo(Element modelEle, Member_cart m, CartCompModel ccm) {
        Element newLi = modelEle.clone();
        //商品IDdiv
        Elements idEle = newLi.select(".j_productId");
        //规格ID
        Elements attrIdEle = newLi.select(".j_attrId");
        //商品名称div
        Elements nameEle = newLi.select(".j_productName");
        //商品价格div
        Elements proPriceEle = newLi.select(".j_productPrice");
        //商品数量div
        Elements proNumEle = newLi.select(".j_productNum");
        Elements productImgEle = newLi.select(".j_productImg");
        Elements productUrlEle = newLi.select(".j_productUrl");
        //设置数据
        Goods_product product=m.getGoodsProduct();
        productUrlEle.attr("href", contextPath + ccm.getProductUrl() + "?sku=" + m.getSku());
        idEle.html(product.getId());
        attrIdEle.html(m.getSku());
        nameEle.html(product.getName());
        nameEle.attr("href", contextPath + ccm.getProductUrl() + "?sku=" + m.getProductId());
        proPriceEle.html(String.valueOf(MoneyUtil.fenToYuan(m.getPrice())));
        proNumEle.html(String.valueOf(m.getNum()));
        productImgEle.attr("src", Strings.isEmpty(m.getImgurl()) ? "" :m.getImgurl());
        return newLi;
    }

    /**
     * 渲染简易模块的数据
     *
     * @param compEle
     * @param ccm
     */
    private void loadUpFrame(Element compEle, CartCompModel ccm, Map m) {
        List<Member_cart> carts=(List<Member_cart>)m.get("cartProduct");
        int totalPrice=0;
        if(!Lang.isEmpty(carts)){
           for(Member_cart cart:carts){
               totalPrice +=(cart.getPrice())*(cart.getNum());
           }
        }
        Element cartUrlEle = compEle.getElementById(ccm.getCompId() + "_cartUrl");
        cartUrlEle.attr("href", contextPath + ccm.getCartShopUrl());
        //图标
        Element imgEle = compEle.getElementById(ccm.getCompId() + "_img");
        imgEle.attr("url", ccm.getImgUrl());
        //显示商品数量
        Element pNumEle = compEle.getElementById(ccm.getCompId() + "_totalPrice");
        //设置商品总价
        if (m != null) {
            pNumEle.html(String.valueOf(MoneyUtil.fenToYuan(totalPrice)));
        } else {
            pNumEle.html("0");
        }
    }
}
