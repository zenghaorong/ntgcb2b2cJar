package com.aebiz.app.dec.commons.comps.productlist;

import com.aebiz.app.dec.commons.comps.productlist.vo.ProductListCompModel;
import com.aebiz.app.dec.commons.comps.productlist.vo.ProductModel;
import com.aebiz.app.dec.commons.comps.productlist.vo.ProductPageModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.goods.modules.services.GoodsPriceService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.commons.utils.MoneyUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by 金辉 on 2016/12/21.
 */
@Controller
@RequestMapping("/productListComp")
public class ProductListCompController extends BaseCompController {
    @Autowired
    public ProductForCompService productForCompService;
    /*@Autowired
    public StoreInfoForCompService storeInfoForCompService;*/
    private String contextPath = "";
    @Autowired
    private GoodsPriceService goodsPriceService;
    @Override
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {
        Document doc = Jsoup.parse(pageViewHtml);
        String compId = bcm.getCompId();
        Element compEle = doc.getElementById(compId);
        ProductListCompModel pcm = (ProductListCompModel) bcm;
        ProductPageModel ppm = doSearch(wpm, pcm);
        contextPath = (String) wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
        Element productsEle = compEle.getElementById(compId + "_products");
        //渲染商品列表
        renderProductList(ppm, productsEle, compId, pcm);
        super.delCompLoading(doc, pcm);
        return doc.html();
    }

    @RequestMapping("/toParamsDesign")
    public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
        String toUrl = (String) request.getAttribute("toParamsJspURL");
        return toUrl;
    }

    @RequestMapping("/ajaxLoadData")
    @ResponseBody
    public ProductPageModel ajaxLoadData(@RequestParam("compId") String compId, HttpServletRequest request) throws UnsupportedEncodingException {
        Map requestMap = getParameterMap(request);
        Pagination dataMap = null;
        String pageShow = request.getParameter("pageShow");
        Map<String, String> paramMap = initParamMap(requestMap, pageShow);
        String nowPage = paramMap.get("nowPage");
        if(!Strings.isEmpty(paramMap.get("keyword"))){
            dataMap = productForCompService.search("", paramMap.get("keyword"), paramMap.get("categoryUuid"),
                    Integer.parseInt(Strings.isEmpty(pageShow) ? "0" : pageShow), Integer.parseInt(Strings.isEmpty(nowPage) ? "0" : nowPage),
                    paramMap.get("sortBy"), paramMap.get("sortType"), paramMap.get("startPrice"),
                    paramMap.get("endPrice"));
        }
        ProductPageModel ppm = castToProductPageModel(dataMap);
        if(ppm!=null) {
            ppm.setPageShow(request.getParameter("pageShow"));
            ppm.setNowPage(paramMap.get("nowPage").toString());
        }
        return ppm;
    }

    @Override
    public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
        ProductListCompModel pcm = (ProductListCompModel) bcm;
        designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(pcm));
        designJs = designJs.replaceAll("\\$_compId", pcm.getCompId());
        return designJs;
    }

    @RequestMapping("/collectProduct")
    @ResponseBody
    public Map collectProduct(@RequestParam("productUuid") String productUuid) {
        Map paramMap = new HashMap();
        String result = productForCompService.addProductFavorite(productUuid);
        paramMap.put("nowPage", "0");
        paramMap.put("pageShow", "10");
        int count = productForCompService.getProductFavorite();
        Map returnMap = new HashMap();
        if ("1".equals(result)) {
            returnMap.put("code", 1);
        } else {
            returnMap.put("code", result);
        }
        returnMap.put("totalNum", count);
        return returnMap;
    }

    /**
     * 渲染商品列表
     *
     * @param ppm
     * @param productsEle
     * @param compId
     */
    private void renderProductList(ProductPageModel ppm, Element productsEle, String compId, ProductListCompModel pcm) {
        Element noResultEle = productsEle.getElementById(compId + "_noResult");
        Element productLoopEle = productsEle.getElementById(compId + "_productLoop");
        Element productTotalNumEle = productsEle.getElementById(compId + "_productTotalNum");
        Element productEle = productsEle.select(".j_product").first();
        if(ppm==null){
            productEle.remove();
        }else {
            List productList = ppm.getProductList();
            if (productList.size() > 0) {
                productTotalNumEle.html(ppm.getTotalNum());
                noResultEle.remove();
                for (Object m : productList) {
                    ProductModel model = (ProductModel) m;
                    Element newEle = productEle.clone();
                    //渲染商品Element
                    renderProductInfo(newEle, model, pcm);
                    productLoopEle.appendChild(newEle);
                }
                productEle.remove();
            } else {//若无商品，提示无商品
                productEle.remove();
            }
        }
    }

    /**
     * 渲染商品信息
     *
     * @param newEle 商品html
     * @param model  商品实体
     */
    private void renderProductInfo(Element newEle, ProductModel model, ProductListCompModel pcm) {
        /*商品ID隐藏标签*/
        Element productIdEle = newEle.select(".j_productId").first();
        //价格
        Element picEle = newEle.select(".j_pic").first();
        //图片
        Element picHoverEle = newEle.select(".j_picHover").first();
        //评分
        Element scoreEle = newEle.select(".j_score").first();
        //商品名称
        Element productNameEle = newEle.select(".j_productName").first();
        /*商品详情页路径*/
        Element productUrlEle = newEle.select(".j_productUrl").first();
        //商品推荐语
        Element recommendEle = newEle.select(".j_recommend").first();
        //商品价格
        Element priceEle = newEle.select(".j_price").first();
        //所属商户名称
        Element storeName = newEle.select(".j_storeName").first();
        //所属商户logo
        Element storeLogo = newEle.select(".j_storeLogo").first();
        productIdEle.html(model.getSku());
        picEle.attr("src", Strings.isEmpty(model.getPic()) ? "" : model.getPic());
        picEle.attr("alt", model.getProductName());
        picHoverEle.attr("src", Strings.isEmpty(model.getPic()) ? "" : model.getPic());
        picHoverEle.attr("alt", model.getProductName());
        scoreEle.attr("style", "width:" + model.getScore() * 20 + "%");
        productNameEle.html(model.getProductName());
        productNameEle.attr("href",contextPath+pcm.getProductInfoUrl()+"?sku="+ model.getSku()+"");
        productUrlEle.attr("href", contextPath+pcm.getProductInfoUrl()+"?sku="+ model.getSku()+"");
        recommendEle.html(model.getRecommend());
        priceEle.html(MoneyUtil.fenToYuan((int)model.getPrice()) + "");
        storeName.attr("title", model.getStoreName());
        String logo=model.getStoreLogo();
        if(Strings.isEmpty(logo)){
        }else{
            storeLogo.attr("src",model.getStoreLogo());
        }

    }

    /**
     * 调用接口进行查询，
     * 并将结果传递给筛选组件
     *
     * @param wpm 页面model
     * @param pcm 组件model
     */
    private ProductPageModel doSearch(WebPageModel wpm, ProductListCompModel pcm) {
        Pagination dataMap =null;
        //获取参数传递map
        Map params = wpm.getMapPageParams();

//            //获取URL传参
//            Map requestParams = (Map) params.get("requestParams");
       //构造接口调用参数
        Map<String,String> paramMap = initParamMap(params, pcm.getPageShow());
        if(!Strings.isEmpty(paramMap.get("keyword"))){
            dataMap =dataMap=productForCompService.search("",paramMap.get("keyword"),paramMap.get("categoryUuid"),Integer.parseInt(paramMap.get("pageShow")),Integer.parseInt(paramMap.get("nowPage")),paramMap.get("sortBy"),paramMap.get("sortType"),paramMap.get("startPrice"),paramMap.get("endPrice"));
        }
        //传递给筛选组件使用
        params.put(pcm.getShareDataName() + "_searchResult", dataMap);

        ProductPageModel ppm = castToProductPageModel(dataMap);
        if(ppm!=null)
            ppm.setPageShow(pcm.getPageShow());
        return ppm;
    }

    /**
     * 构造接口调用参数
     *
     * @param requestParams
     * @param pageShow
     * @return
     */
    private Map initParamMap(Map requestParams, String pageShow) {
        //关键字
        String keyword = (String) requestParams.get("keyword");
        //当前页码
        String nowPage = Strings.isEmpty((String) requestParams.get("nowPage"))?"1":(String) requestParams.get("nowPage");
         String categoryUuid = (String) requestParams.get("categoryUuid");
        String startPrice = (String) requestParams.get("startPrice");
        String endPrice = (String) requestParams.get("endPrice");
        String sortBy = (String) requestParams.get("sortBy");
        String sortType = (String) requestParams.get("sortType");
        String attrValue = (String) requestParams.get("attrValue");
        Map paramMap = new HashMap();
        paramMap.put("nowPage", nowPage != null ? nowPage : "1");
        paramMap.put("pageShow", pageShow);
        if (keyword != null && !Strings.isEmpty(keyword)) {
            paramMap.put("keyword", keyword);
        } else {
            paramMap.put("keyword", "");
        }
        if (categoryUuid != null && !Strings.isEmpty(categoryUuid)) {
            paramMap.put("categoryUuid", categoryUuid);
        }
        if (startPrice != null && !Strings.isEmpty(startPrice)) {
            paramMap.put("startPrice", startPrice);
        }
        if (endPrice != null && !Strings.isEmpty(endPrice)) {
            paramMap.put("endPrice", endPrice);
        }
        if (sortBy != null && !Strings.isEmpty(sortBy)) {
            paramMap.put("sortBy", sortBy);
        }
        if (sortType != null && !Strings.isEmpty(sortType)) {
            paramMap.put("sortType", sortType);
        }
        if (attrValue != null && !Strings.isEmpty(attrValue)) {
            paramMap.put("attrValue", handleAttrValue(attrValue));
        }
        return paramMap;
    }

    /**
     * 处理并拼接其他参数属性
     *
     * @param attrValue
     * @return
     */
    private String handleAttrValue(String attrValue) {
        String newAttr = "";
        String[] attrs = attrValue.split("@");
        for (int i = 0; i < attrs.length; ++i) {
            String attr = attrs[i];
            if (Strings.isEmpty(attr)) {
                continue;
            }
            String[] keyAndValu = attr.split("_");
            newAttr += keyAndValu[0] + "=" + keyAndValu[1];
            if (i != attrs.length - 1) {
                newAttr += "&";
            }
        }
        return newAttr;
    }

    /**
     * 将接口返回数据转换成商品分页实体
     *
     * @param dataMap
     * @return
     */
    private ProductPageModel castToProductPageModel(Pagination dataMap) {
        if(dataMap==null){
            return null;
        }
        dataMap.getList();
        ProductPageModel model = new ProductPageModel();
        List productList =  dataMap.getList();
        int totalNum = dataMap.getTotalCount();
        List newList = new ArrayList();
        if (productList != null) {
            for (Object map : productList) {
                //将map转换为productModel
                ProductModel m = castToProduct((ProductDTO) map);
                newList.add(m);
            }
        }
        model.setProductList(newList);
        model.setTotalNum(totalNum + "");
        return model;
    }

    /**
     * 将map转换成model
     *
     * @param model
     * @return
     */
    private ProductModel castToProduct(ProductDTO model) {
        ProductModel m = new ProductModel();
        if (model == null) {
            return null;
        }
        m.setProductId(model.getUuid());
        m.setPic(model.getPic());
        m.setRecommend(model.getRecommend());
        m.setScore(model.getScore());
        m.setPrice(model.getPrice());
        m.setStoreName(model.getStoreName());
        m.setStoreUuid(model.getStoreUuid());
        m.setProductName(model.getName());
        m.setSku(model.getSkuNo());
        Map mapParam = new HashMap();
        mapParam.put("storeUuid", m.getStoreUuid());
        //调用接口获取商品对应商户信息
       // String logoUrl = storeInfoForCompService.getStoreLogoUrl(m.getStoreUuid());
        String logoUrl="";
//        StoreInfoInteractiveModel storeInfo = (StoreInfoInteractiveModel) dataMap.get("storeInfo");
        if (logoUrl != null) {
            m.setStoreLogo(logoUrl);
        }
        return m;
    }

    /**
     * 从request中获得参数Map，并返回可读的Map
     *
     * @param request
     * @return
     */
    private Map getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }
}
