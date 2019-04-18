package com.aebiz.app.dec.commons.comps.screen;


import com.aebiz.app.dec.commons.comps.productlist.vo.ScreenCategoryModel;
import com.aebiz.app.dec.commons.comps.screen.vo.AttributeModel;
import com.aebiz.app.dec.commons.comps.screen.vo.BrandModel;
import com.aebiz.app.dec.commons.comps.screen.vo.ScreenCompModel;
import com.aebiz.app.dec.commons.comps.screen.vo.ScreenModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.goods.modules.models.Goods_brand;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.services.GoodsBrandService;
import com.aebiz.app.store.modules.models.Store_goodsclass;
import com.aebiz.app.store.modules.services.StoreGoodsclassService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductConvertHelper;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.SelectAttributeJsonDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.store.BrandDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.page.Pagination;
import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by 金辉 on 2016/12/21.
 */
@RequestMapping("/screenComp")
@Controller
public class ScreenCompController extends BaseCompController {
    @Autowired
    private ProductForCompService productForCompService;
    @Autowired
    private GoodsBrandService goodsBrandService;

    @Autowired
    private StoreGoodsclassService storeGoodsclassService;
    @Override
    public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {
        Document doc = Jsoup.parse(pageViewHtml);
        String compId = bcm.getCompId();
        Element compEle = doc.getElementById(compId);
        ScreenCompModel scm = (ScreenCompModel) bcm;
        ScreenModel screenModel = doSearch(wpm, scm);
        //渲染已添加的筛选属性
        Map params = wpm.getMapPageParams();
//        //获取URL传参
//        Map requestParams = (Map) params.get("requestParams");
        if(params!=null){
            String attrValue = (String) params.get("attrValue");
            String categoryUuid = (String) params.get("categoryUuid");
            String categoryName = this.getCategoryName(categoryUuid);
            if(!Strings.isEmpty(categoryName)) {
                if(attrValue!=null&&!Strings.isEmpty(attrValue)){
                    attrValue = "分类_" + categoryName + "@" + attrValue;
                }else{
                    attrValue = "分类_" + categoryName;
                }
            }
            if(attrValue!=null&&!Strings.isEmpty(attrValue)){
                //渲染选中的属性
                renderChooseAttr(compEle,attrValue,compId);
            }
        }
        //渲染筛选模块
        renderScreenMoudle(compEle,compId,screenModel,wpm);
        super.delCompLoading(doc,bcm);
        return doc.html();
    }

    @RequestMapping("/toParamsDesign")
    @Override
    public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
        String toUrl = (String) request.getAttribute("toParamsJspURL");
        return toUrl;
    }

    @RequestMapping("/ajaxLoadData")
    @ResponseBody
    public ScreenModel ajaxLoadData(@RequestParam("compId")String compId, HttpServletRequest request) {
        Map requestMap = getParameterMap(request);
        Pagination dataMap  =null;
        String pageShow = request.getParameter("pageShow");
        Map<String,String> paramMap = initParamMap(requestMap,pageShow);
        String nowPage = paramMap.get("nowPage");
        String categoryUuid="";
        if(!Strings.isEmpty(paramMap.get("categoryUuid"))){
            categoryUuid=paramMap.get("categoryUuid");
        }
        if(!Strings.isEmpty(paramMap.get("keyword"))){
            dataMap  = productForCompService.doSearch("", paramMap.get("keyword"), paramMap.get("categoryUuid"),
                    Integer.parseInt(Strings.isEmpty(pageShow)?"8":pageShow), Integer.parseInt(Strings.isEmpty(nowPage)?"1":nowPage),
                    paramMap.get("sortBy"), paramMap.get("sortType"), paramMap.get("startPrice"),
                    paramMap.get("endPrice"));
        }
        ScreenModel screenModel = castToScreenModel(dataMap,categoryUuid);
        return screenModel;
    }

    @Override
    public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
        ScreenCompModel scm = (ScreenCompModel)bcm;
        designJs = designJs.replaceAll("\\$_compConfig_\\$", JSON.toJSONString(scm));
        designJs = designJs.replaceAll("\\$_compId", scm.getCompId());
        return designJs;
    }
    @RequestMapping("/getCategoryName")
    @ResponseBody
    public String getCategoryName(@RequestParam("categoryUuid")String categoryUuid){
        ProductCategoryDTO productCategory = productForCompService.getProductCateByUuid(categoryUuid);
        if(productCategory!=null){
            return productCategory.getCategoryName();
        }else{
            return null;
        }
    }

    /**
     * 获取选中的分类名称
     * @param categoryUuid
     * @return
     */
    private String findChooseCatName(String categoryUuid,List<ScreenCategoryModel> categoryModels){
        if(!Strings.isEmpty(categoryUuid)&&categoryModels == null&&categoryModels.size()!=0) {
            for (ScreenCategoryModel m : categoryModels) {
                if (categoryUuid.equals(m.getUuid())) {
                    return m.getCategoryName();
                }
            }
        }
        return null;
    }
    /**
     * 渲染选中的筛选条件div
     * @param compEle
     * @param attrValue
     * @param compId
     */
    private void renderChooseAttr(Element compEle, String attrValue, String compId){
        Element chooseAttrEle = compEle.getElementById(compId+"_chooseAttributesDIV");
        Element loopEle = chooseAttrEle.getElementById(compId+"_chooseAttributes");
        Element attrEle = loopEle.select(".j_attribute").first();
        String[] attrArray = attrValue.split("@");
        for(String attr:attrArray){
            Element newEle = attrEle.clone();
            Element nameEle = newEle.select(".j_attrName").first();
            Element valueEle = newEle.select(".j_attrValue").first();
            if(Strings.isEmpty(attr)){
                continue;
            }
            String[] nameAndValue = attr.split("_");
            newEle.attr("data-name",nameAndValue[0]);
            if("attributeBrand".equals(nameAndValue[0])){
                nameEle.html("品牌");
            }else if("attributePrice".equals(nameAndValue[0])) {
                nameEle.html("价格");
            }else{
                nameEle.html(nameAndValue[0]);
            }
            valueEle.html(nameAndValue[1]);
            loopEle.appendChild(newEle);
        }
        attrEle.remove();
        chooseAttrEle.attr("style","display:block");
    }
    /**
     * 调用接口进行查询，
     * 并将结果传递给商品列表组件
     * @param wpm   页面model
     * @param scm   组件model
     */
    private ScreenModel doSearch(WebPageModel wpm,ScreenCompModel scm){
        Pagination dataMap=null;
        //获取参数传递map
        Map params = wpm.getMapPageParams();
        Map reslutMap = (Map) params.get(scm.getShareDataName()+"_searchResult");
        String categoryUuid="";
//            //获取URL传参
       Map requestParams = (Map) params.get("requestParams");
        //构造接口调用参数
        Map<String,String> paramMap = initParamMap(params, scm.getPageShow());
        String pageShow = paramMap.get("pageShow");
        String nowPage = paramMap.get("nowPage");
        categoryUuid=paramMap.get("categoryUuid");
        if(!Strings.isEmpty(paramMap.get("keyword"))){
            dataMap = productForCompService.doSearch("", paramMap.get("keyword"), paramMap.get("categoryUuid"),
                    Integer.parseInt(Strings.isEmpty(pageShow)?"0":pageShow), Integer.parseInt(Strings.isEmpty(nowPage)?"0":nowPage),
                    paramMap.get("sortBy"), paramMap.get("sortType"), paramMap.get("startPrice"),
                    paramMap.get("endPrice"));
        }
        params.put(scm.getShareDataName()+"_searchResult",dataMap);
        return castToScreenModel(dataMap,categoryUuid);
    }

    /**
     * 渲染商品筛选模块
     * @param compEle       组件element
     * @param compId        组件ID
     * @param screenModel   筛选model
     */
    private void renderScreenMoudle(Element compEle, String compId, ScreenModel screenModel, WebPageModel wpm){
        Map requestParams = (Map) wpm.getMapPageParams();
        String attrValue = Strings.isEmpty((String) requestParams.get("attrValue"))?"":(String) requestParams.get("attrValue");
        String categoryUuid = Strings.isEmpty((String) requestParams.get("categoryUuid"))?"":(String) requestParams.get("categoryUuid");
        if(!Strings.isEmpty(categoryUuid)){
            attrValue="categoryUuid_"+categoryUuid+"@"+attrValue;
        }
        List brandList = screenModel.getBrands();
        List priceList = screenModel.getPrices();
        List categoryList = screenModel.getCateList();
        List attributeList = screenModel.getAttributeList();
        if(!isHaveQuery("attributeBrand",attrValue)&&brandList!=null&&brandList.size()>0){
            //渲染品牌
            renderBrandLoop(compEle, compId, brandList);
        }else{//如果已经查询了该属性 就不显示该删选条件
            Element brandModule = compEle.getElementById(compId+"_brandModule");
            brandModule.remove();
        }
        if(!isHaveQuery("attributePrice",attrValue)&&priceList!=null&&priceList.size()>0){
            //渲染价格
            renderPrice(compEle,compId,priceList);
        }else{//如果已经查询了该属性 就不显示该删选条件
            Element priceModule = compEle.getElementById(compId+"_priceModule");
            priceModule.remove();
        }
        if(categoryList!=null&&categoryList.size()>0){
            //渲染分类
            renderCategory(compEle, categoryList,compId);
        }else{//如果已经查询了该属性 就不显示该删选条件
            Element categoryModule = compEle.getElementById(compId+"_categoryModule");
            categoryModule.remove();
        }
        //渲染其他属性
        loopAddOtherEle(compEle,attributeList,attrValue,compId);
    }

    /**
     * 判断属性是否已在查询中
     * @param param
     * @param attrValue
     * @return
     */
    private boolean isHaveQuery(String param,String attrValue){
        if(attrValue!=null){
            String[] attrs = attrValue.split("@");
            if(attrs!=null){
                for(String attr:attrs){
                    String[] keyAndValue = attr.split("_");
                    if(keyAndValue!=null){
                        if( keyAndValue[0].equals(param)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     *  循环渲染品牌过滤
     * @param compEle   组件Element
     * @param compId    组件ID
     * @param brandList 品牌list
     */
    private void renderBrandLoop(Element compEle, String compId, List<BrandModel> brandList) {
        Element brandLoopEle = compEle.getElementById(compId + "_brandLoop");
        Element brandEle = brandLoopEle.select(".j_brand").first();
        for (BrandModel brandModel : brandList) {//渲染默认全部品牌Element
            String pinyin = brandModel.getBrandName();
            char firstChar = pinyin.charAt(0);
            firstChar = Character.toLowerCase(firstChar);
            Element newEle = brandEle.clone();
            //渲染品牌
            renderBrandInfo(newEle,brandModel,firstChar,compId);
            brandLoopEle.appendChild(newEle);
        }
        //渲染查看更多时全部的品牌
        brandEle.remove();
    }

    /**
     * 渲染品牌
     * @param newEle        品牌模板克隆
     * @param brandModel    品牌model
     * @param firstChar     品牌名称拼音首字母
     */
    private void renderBrandInfo(Element newEle, BrandModel brandModel, char firstChar, String compId){
        Element brandUrlEle = newEle.select(".j_brandUrl").first();
        Element brandImgEle = newEle.select(".j_brandImgUrl").first();
        Element brandNameEle = newEle.select(".j_brandName").first();
//            brandUrlEle.attr("href","/webpage/run?pageUuid=123&attrValue=attributeBrand_"+brandModel.getBrandName());
//            brandUrlEle.attr("");
        brandUrlEle.attr("onclick", "doSearch_"+compId+"(this,'attributeBrand','" + brandModel.getBrandName() + "')");
        brandUrlEle.attr("title", brandModel.getBrandName());
        newEle.attr("data-initial", String.valueOf(firstChar));
        brandImgEle.attr("src", brandModel.getImageUrl1());
        brandNameEle.html(brandModel.getBrandName());
    }

    /**
     * 渲染价格过滤
     * @param compEle
     * @param compId
     * @param priceList
     */
    private void renderPrice(Element compEle, String compId, List<String> priceList){
        Element loopEle = compEle.getElementById(compId+"_priceLoop");
        Element moduleEle = loopEle.select(".j_priceModule").first();
        for(int i=0;i<priceList.size();++i){
            String str = priceList.get(i);
            Element newEle = moduleEle.clone();
            Element valueEle =newEle.select(".j_value").first();
            if(i==0){
                str = "0-"+str;
            }else if(i==priceList.size()-1){
                str+="以上";
            }
            valueEle.attr("onclick","doSearch_"+compId+"(this,'attributePrice','"+str+"')");
            valueEle.html(str);
            loopEle.appendChild(newEle);
        }
        //删除模板
        moduleEle.remove();
    }

    /**
     * 渲染分类筛选
     * @param compEle
     * @param categoryModels
     * @param compId
     */
    private void renderCategory(Element compEle, List<ScreenCategoryModel> categoryModels, String compId){
        Element loopEle = compEle.select(".j_cateLoop").first();
        Element moduleEle = loopEle.select(".j_cateModule").first();
        for(ScreenCategoryModel categoryModel:categoryModels){
            Element newEle = moduleEle.clone();
            Element valueEle = newEle.select(".j_value").first();
            Element categoryUuidEle = newEle.select(".j_categoryUuid").first();
            categoryUuidEle.val(categoryModel.getUuid());
            valueEle.html(categoryModel.getCategoryName());
            valueEle.attr("onclick", "doSearch_" + compId + "(this,'categoryUuid','"+categoryModel.getUuid()+"')");
            loopEle.appendChild(newEle);
        }
        moduleEle.remove();
    }
    /**
     * 循环添加筛选属性
     * @param comEle
     * @param attributeModels
     */
    private void loopAddOtherEle(Element comEle, List<AttributeModel> attributeModels, String attrValues, String compId){
        Element otherEle = comEle.select(".j_other").first();
        if(attributeModels !=null && attributeModels.size()>0){
            for(AttributeModel attributeModel:attributeModels){
                if(isHaveQuery(attributeModel.getAttributeName(),attrValues)){
                    continue;
                }
                Element newEle = otherEle.clone();
                Element attrNameEle = newEle.select(".j_attrName").first();
                attrNameEle.html(attributeModel.getAttributeName()+"：");
                renderOtherAttr(newEle,attributeModel.getAttributeName(),attributeModel.getSelectValue(),attributeModel.getUnit(),compId);
                comEle.appendChild(newEle);
            }
        }
        otherEle.remove();
    }
    /**
     * 渲染其他数据
     * @param otherEle          其他属性Element
     * @param attrName          属性名称
     * @param attributeList     属性值list
     * @param unit              单位
     */
    private void renderOtherAttr(Element otherEle, String attrName, List<String> attributeList, String unit, String compId){
        Element loopEle = otherEle.select(".j_attrLoop").first();
        Element moduleEle = otherEle.select(".j_attrModule").first();
        for(int i=0;i<attributeList.size();++i){
            String str = attributeList.get(i);
            Element newEle = moduleEle.clone();
            Element valueEle =newEle.select(".j_value").first();
            valueEle.attr("onclick","doSearch_"+compId+"(this,'"+attrName+"','"+str+"')");
            valueEle.html(str+unit);
            loopEle.appendChild(newEle);
        }
        //删除模板
        moduleEle.remove();
    }
    /**
     * 转换实体
     * @param p
     * @return
     */
    private ScreenModel  castToScreenModel(Pagination p,String categoryUuid){
        ScreenModel screenModel = new ScreenModel();
        if(!Lang.isEmpty(p)){
            List<BrandDTO> brandDTOs=new ArrayList<BrandDTO>();
            List<Goods_main> mains=(List<Goods_main>)p.getList();
            List<Store_goodsclass> allStoreList=new ArrayList<>();
            List<String>  strList=new ArrayList<>();
            if(mains!=null && mains.size()>0){
                for(Goods_main mainModel:mains) {
                    List<Store_goodsclass> storeClasss=mainModel.getStoreGoodsClassList();
                    if(storeClasss !=null && storeClasss.size()>0){
                        for(Store_goodsclass goodsclass:storeClasss){
                            if(Strings.isEmpty(goodsclass.getParentId())){
                                Store_goodsclass goodsclass1= storeGoodsclassService.fetch(goodsclass.getParentId());
                                if(!Lang.isEmpty(goodsclass1)){
                                    if(!strList.contains(goodsclass1.getId())){
                                        strList.add(goodsclass1.getId());
                                        allStoreList.add(storeGoodsclassService.fetch(goodsclass.getParentId()));
                                    }
                                }
                            }else{
                                Store_goodsclass goodsclass1= storeGoodsclassService.fetch(goodsclass.getParentId());
                                if(!Lang.isEmpty(goodsclass1)) {
                                    if (!strList.contains(goodsclass1.getId())) {
                                        strList.add(goodsclass1.getId());
                                        allStoreList.add(storeGoodsclassService.fetch(goodsclass.getParentId()));
                                    }
                                }
                            }
                        }
                    }
                    Goods_brand brand=mainModel.getGoodsBrand();
                    if(brand !=null){
                        BrandDTO brandDTO=new BrandDTO();
                        brandDTO.setBrandEnName("");
                        brandDTO.setBrandName(brand.getName());
                        brandDTO.setBrandUuid(brand.getId());
                        brandDTO.setImgUrl1(brand.getImgurl());
                        brandDTO.setImgUrl2(brand.getImgurl());
                        brandDTO.setNote(brand.getNote());
                        brandDTOs.add(brandDTO);
                    }
                }
            }
            List priceList =null;
            List<SelectAttributeJsonDTO> attributeList =null;
            List<ProductCategoryDTO> cateList =null;
            if(!Strings.isEmpty(categoryUuid)){
                List<Store_goodsclass> secStoreClasss=storeGoodsclassService.getAllProductCategoryFront(categoryUuid);
                cateList=ProductConvertHelper.convertProductCategoryToDTOs(secStoreClasss);
            }else{
                cateList =ProductConvertHelper.convertProductCategoryToDTOs(allStoreList);
            }
            List castBrandList = new ArrayList();
            List castAttributeList = new ArrayList();
            List castCateList = new ArrayList();
            if(brandDTOs!=null) {
                for (BrandDTO productBrandModel : brandDTOs) {
                    castBrandList.add(castToBrandModel(productBrandModel));
                }
            }
            if(attributeList!=null) {
                for (SelectAttributeJsonDTO json : attributeList) {
                    castAttributeList.add(castToAttributeModel(json));
                }
            }
            if(cateList!=null) {
                for (ProductCategoryDTO json : cateList) {
                    castCateList.add(castToCategoryModel(json));
                }
            }
            screenModel.setAttributeList(castAttributeList);
            screenModel.setCateList(castCateList);
            screenModel.setBrands(castBrandList);
            screenModel.setPrices(priceList);
        }
        return screenModel;
    }

    /**
     * 转换实体
     * @param productBrandModel
     * @return
     */
    private BrandModel castToBrandModel(BrandDTO productBrandModel){
        BrandModel brandModel = new BrandModel();
        brandModel.setBrandEnName(productBrandModel.getBrandEnName());
        brandModel.setBrandName(productBrandModel.getBrandName());
//        brandModel.setChooseOrNo(productBrandModel.getChooseOrNo());
        brandModel.setImage1(productBrandModel.getImgUrl1());
        brandModel.setImage2(productBrandModel.getImgUrl2());
        brandModel.setImageUrl1(productBrandModel.getImgUrl1());
        brandModel.setImageUrl2(productBrandModel.getImgUrl2());
        brandModel.setNote(productBrandModel.getNote());
        brandModel.setParentUuid(productBrandModel.getParentUuid());
        brandModel.setUuid(productBrandModel.getBrandUuid());
        return brandModel;
    }

    /**
     * 转换实体
     * @param attributeJson
     * @return
     */
    private AttributeModel castToAttributeModel(SelectAttributeJsonDTO attributeJson){
        AttributeModel attributeModel = new AttributeModel();
        attributeModel.setAttributeEnName(attributeJson.getAttributeEnName());
        attributeModel.setAttributeName(attributeJson.getAttributeName());
        attributeModel.setAttributeUuid(attributeJson.getAttributeUuid());
        attributeModel.setSelectValue(attributeJson.getSelectValue());
        attributeModel.setType(attributeJson.getType());
        attributeModel.setUnit(attributeJson.getUnit());
        return attributeModel;
    }

    /**
     * 转换实体
     * @param categoryModel
     * @return
     */
    private ScreenCategoryModel castToCategoryModel(ProductCategoryDTO categoryModel){
        ScreenCategoryModel screenCategoryModel = new ScreenCategoryModel();
        screenCategoryModel.setUuid(categoryModel.getCategoryUuid());
        screenCategoryModel.setCategoryName(categoryModel.getCategoryName());
        return screenCategoryModel;
    }
    /**
     * 构造接口调用参数
     * @param requestParams
     * @param pageShow
     * @return
     */
    private Map initParamMap(Map requestParams,String pageShow){
        //关键字
        String keyword = (String) requestParams.get("keyword");
        //当前页码
        String nowPage = (String) requestParams.get("nowPage");
        String categoryUuid = (String) requestParams.get("categoryUuid");
        String sortBy = (String) requestParams.get("sortBy");
        String sortType = (String) requestParams.get("sortType");
        String attrValue = (String) requestParams.get("attrValue");
        Map paramMap = new HashMap();
        paramMap.put("nowPage",nowPage!=null?nowPage:"1");
        paramMap.put("pageShow",pageShow);
        if(keyword!=null){
            paramMap.put("keyword",keyword);
        }else{
            paramMap.put("keyword","");
        }
        if(categoryUuid!=null){
            paramMap.put("categoryUuid",categoryUuid);
        }
        if(sortBy!=null){
            paramMap.put("sortBy",sortBy);
        }
        if(sortType!=null){
            paramMap.put("sortType",sortType);
        }
        if(attrValue!=null){
            paramMap.put("attrValue",handleAttrValue(attrValue));
        }
        return paramMap;
    }
    /**
     * 处理并拼接其他参数属性
     * @param attrValue
     * @return
     */
    private String handleAttrValue(String attrValue){
        String newAttr = "";
        if(Strings.isEmpty(attrValue)){
            return null;
        }
        String[] attrs = attrValue.split("@");
        for(int i=0;i<attrs.length;++i){
            String attr = attrs[i];
            if(Strings.isEmpty(attr)){
                continue;
            }
            String[] keyAndValue = attr.split("_");
            newAttr+=keyAndValue[0]+"="+keyAndValue[1];
            if(i!=attrs.length-1){
                newAttr+="&";
            }
        }

        return newAttr;
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
