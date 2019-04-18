package com.aebiz.app.dec.commons.service.impl;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.Datatable;
import com.aebiz.app.goods.commons.utils.GoodsUtil;
import com.aebiz.app.goods.modules.models.*;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.order.modules.models.Order_goods_feedback;
import com.aebiz.app.order.modules.services.OrderGoodsFeedbackService;
import com.aebiz.app.sales.modules.models.Sales_rule_goods;
import com.aebiz.app.sales.modules.services.SalesRuleGoodsService;
import com.aebiz.app.store.modules.models.Store_goodsclass;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreGoodsclassService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.web.commons.es.EsService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.*;
import com.aebiz.app.web.modules.controllers.open.dec.exception.ProductException;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.commons.utils.StringUtil;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.nutz.dao.Cnd;
import org.elasticsearch.common.text.Text;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 商品相关业务实现
 * Created by Aebiz_yjq on 2016/12/14.
 */
@Service
public class ProductForCompServiceImpl implements ProductForCompService {
    @Autowired
    private GoodsClassService goodsClassService;
    @Autowired
    private PropertiesProxy config;

    @Autowired
    private EsService esService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
     private StoreMainService storeMainService;
    @Autowired
    private GoodsImageService goodsImageService;

    @Autowired
    private StoreGoodsclassService storeGoodsclassService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private OrderGoodsFeedbackService orderGoodsFeedbackService;
    @Autowired
    private SalesRuleGoodsService salesRuleGoodsService;
    @Autowired
    private GoodsFavoriteService goodsFavoriteService;
    @Autowired
    private GoodsTypeTabService goodsTypeTabService;
    @Autowired
    private GoodsPriceService goodsPriceService;
    @Autowired
    private GoodsBrandService goodsBrandService;
    private static final Log log = Logs.get();
    /**
     * 获取相似关键词
     * @param keyWord 请求参数：关键词
     * @return 返回结果：联想词列表
     */
    @Override
    public List<String> getLikeKey(String keyWord) {
        List<String> likeKeyStrList = new ArrayList<>();
        if(!Strings.isEmpty(keyWord)){
            Pagination p=doSearch("", keyWord,"", 1, 100,"","","","");
            List<Goods_main> mainList=(List<Goods_main>)p.getList();
            if(mainList !=null && mainList.size()>0){
                for (Goods_main mainModel:mainList) {
                    List<Goods_product> products=mainModel.getProductList();
                    if(products !=null && products.size()>0){
                        for(Goods_product product:products){
                            likeKeyStrList.add(product.getName());
                        }
                    }
                }
            }
            //likeKeyStrList=goodsProductService.getByLikeName(keyWord);
        }
        return likeKeyStrList;
    }
    /**
     * 获取所有的前台分类(取前三级)
     *
     * @return 前台分类列表
     */
    @Override
    public  List<ProductCategoryDTO> getFrontCategory() {
        //获取分类树
        List<Store_goodsclass> goodClassList=storeGoodsclassService.getAllClassGoodsName();

        //转换对象
        return convertProductCatFrontModelToDTO(goodClassList);
    }

    /**
     * 获取平台一级分类
     *
     * @return 返回平台一级分类
     */
    @Override
    public List<ProductCategoryDTO> getFirstLevelCategory() {

        /* 1、获取平台商品分类 */
        List<Goods_class> prodList = goodsClassService.getSubProductCategoryByParentUuid();

        /* 2、定义商品分类DTO列表 */
        List<ProductCategoryDTO> productCategoryDTOs = new ArrayList<>();
        ProductCategoryDTO productCategoryDTO;

        /* 3、循环将商品分类model列表转化为DTO列表 */
        if(!prodList.isEmpty()){
            for(Goods_class model : prodList){
                productCategoryDTO = new ProductCategoryDTO(model.getId(), model.getLocation(), model.getParentId(),
                        "", model.getName(), model.getPath());
                productCategoryDTOs.add(productCategoryDTO);
            }
        }

        /* 4、返回分类列表 */
        return productCategoryDTOs;
    }



    /**
     * 前台分类对象转换为DTO对象
     * @param list
     * @return
     */
    private List<ProductCategoryDTO> convertProductCatFrontModelToDTO( List<Store_goodsclass> list){
        List<ProductCategoryDTO> firstCategoryDTOList = new ArrayList<>();
        if(list!=null && !list.isEmpty()){
            for(Store_goodsclass first :list ){
                ProductCategoryDTO firstLevelDTO = new ProductCategoryDTO(first.getId(), first.getLocation(), first.getParentId(),
                        "", first.getName(), "");

                List<ProductCategoryDTO> subDtos =  convertProductCatFrontModelToDTO(first.getClassList());
                firstLevelDTO.setSubCategoryList(subDtos);
                firstCategoryDTOList.add(firstLevelDTO);
            }
        }

        return firstCategoryDTOList;
    }


    /**
     *商品搜索
     * @param storeUuid 店铺uuid
     * @param keyWords 关键词
     * @param sortBy 排序方式
     * @param sortType 排序类型
     * @param startPrice 开始价格
     * @param endPrice 结束价格
     * @return 返回商品列表
     */
    @Override
    public Pagination search(String storeUuid, String keyWords,String categoryUuid, int pageShow, int nowPage,  String sortBy, String sortType, String startPrice, String endPrice) {
        // return 商品类别uuid 和 匹配的商品列表，格式："{"catUuid":"","productList":""}"
        Pagination pp = doSearch(storeUuid, keyWords,categoryUuid, pageShow, nowPage,sortBy,sortType,startPrice,endPrice);
        //分类uuid用来查询筛选条件
        List<Goods_main> mainList=(List<Goods_main>)pp.getList();
        List<ProductDTO> productDTOs = new ArrayList<>();
        ProductDTO productDTO=null;
        if(mainList !=null && mainList.size()>0){
            for(int x=0;x<mainList.size();x++){
                Goods_main mainModel=mainList.get(x);
                double avgScore=getFeefBack(mainModel);
                Store_main storeMain= storeMainService.fetch(mainModel.getStoreId());
                if(Lang.isEmpty(storeMain)){
                    continue;
                }
                String mainImg="";
                List<Goods_image> images=mainModel.getImageList();

                if(images !=null && images.size()>0){
                    for(Goods_image image:images ){
                        if(image.isDefaultValue()){
                            mainImg=image.getImgAlbum();
                            continue;
                        }
                    }
                }
                List<Store_goodsclass> classe=mainModel.getStoreGoodsClassList();
                //获取商品评分（因为只有货品有评分，所以取商品下面所有货品的评分的平均分）
                List<Goods_product> goodsProduct=mainModel.getProductList();
                if(goodsProduct !=null && goodsProduct.size()>0){
                    for (Goods_product productModle:goodsProduct) {
                        if(productModle !=null){
                            if(productModle.isDefaultValue()){
                                productDTO = new ProductDTO(productModle.getId(), productModle.getName(),"",mainModel.getBrandId(), mainImg,
                                        mainModel.getTitle(), classe.get(0).getId(),classe.get(0).getName(), productModle.getSpec(), productModle.getStoreId(), "",
                                        storeMain.getStoreName(),productModle.getSalePrice(), avgScore, productModle.getSaleNumAll(),0, productModle.getSku(), productModle.getStock().toString());
                                productDTOs.add(productDTO);
                            }
                        }
                    }
                }

            }
        }
        pp.setList(productDTOs);
        return pp;
    }

    /**
     * 获取商品的平均得分
     * @return
     */
    public double getFeefBack(Goods_main goodsMain){
        //因为商品本身没有评价得分。所以这里取得是商品下面的sku。
        List<Goods_product> products=goodsMain.getProductList();
        double avgFeedbackScore=5.00;
        if(!Lang.isEmpty(products)){
            double avgScore=0.00;
            for(Goods_product product:products){
                String str="select avg(a.appScore) from order_goods_feedback a where a.sku=@sku";
                Sql sql= Sqls.create(str).setParam("sku",product.getSku());
               /* sql.setCallback(new SqlCallback() {
                    public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                        String appScore = "";
                        while (rs.next())
                            appScore=rs.getString("appScore");
                        return appScore;
                    }
                });*/
                orderGoodsFeedbackService.dao().execute(sql);
                String appScore=sql.getString();
                if(!Strings.isEmpty(appScore)){
                    avgScore+=Double.parseDouble(sql.getString());
                }else{
                    avgScore+=5.00;
                }
            }
            avgFeedbackScore=avgScore/products.size();
        }
        return avgFeedbackScore;
    }
    public List<Goods_main> getProductList(String[] sku){
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if(sku.length>0 && sku !=null){
            BoolQueryBuilder keywordQuery = QueryBuilders.boolQuery();
            for(int i=0;i<sku.length;i++){
                String skuId=sku[i];
                keywordQuery.should(QueryBuilders.wildcardQuery("productList.id",skuId));
            }
            query.must(keywordQuery);
        }
        SearchRequestBuilder srb = esService.getClient().prepareSearch(config.get("es.index.name"))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setTypes("goods")
                .setQuery(query);
        SearchResponse response = srb.execute().actionGet();
        SearchHits hits = response.getHits();
        List<Goods_main> list = new ArrayList<>();
        for (SearchHit searchHit : hits) {
            Map<String, Object> source = searchHit.getSource();
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            //name高亮
            HighlightField nameField = highlightFields.get("name");
            if (nameField != null) {
                nameField.fragments();
                Text[] fragments = nameField.fragments();
                String tmp = "";
                for (Text text : fragments) {
                    tmp += text;
                }
                source.put("name", tmp);
            }
            //name高亮
            HighlightField titleField = highlightFields.get("title");
            if (titleField != null) {
                Text[] fragments = titleField.fragments();
                String tmp = "";
                for (Text text : fragments) {
                    tmp += text;
                }
                source.put("title", tmp);
            }
            list.add(Lang.map2Object(source,Goods_main.class));
        }
        return list;
    }
    @Override
    public List<ProductDetailDTO> getBatchProductSample(String[] arr) {
        List<ProductDetailDTO> list =new ArrayList<>();
        List<Goods_main> mapMainModels=getProductList(arr);
        List<Goods_product> products=new ArrayList<Goods_product>();
        if(mapMainModels !=null && mapMainModels.size()>0){
            for(Goods_main  mainModel:mapMainModels){
                List<Goods_product> productModels=mainModel.getProductList();
                for(Goods_product goodsProduct :productModels){
                    if(goodsProduct.isDefaultValue()){
                        products.add(goodsProduct);
                        List<Goods_image> images=mainModel.getImageList();
                        String mainImg="";
                        if(images !=null && images.size()>0){
                            for(Goods_image image:images ){
                                if(image.isDefaultValue()){
                                    mainImg=image.getImgAlbum();
                                }
                            }
                        }
                        ProductDetailDTO productDetail =new ProductDetailDTO();
                        //现在跳转商品详情页是根据商品的sku。不是根据商品id.所以直接把productId塞了sku的值
                        productDetail.setProductUuid(goodsProduct.getSku());

                        productDetail.setProductName(goodsProduct.getName());

                        productDetail.setShopPrice(goodsProduct.getSalePrice());

                        productDetail.setSku(goodsProduct.getSku());

                        productDetail.setCenterImageUrl(mainImg);

                        list.add(productDetail);
                    }

                }
            }
        }
        return list;
    }
    /**
     * 根据商品分类uuid获取分类
     * @param categoryUuid 商品分类uuid
     * @return 商品分类dto
     */
    @Override
    public ProductCategoryDTO getProductCateByUuid(String categoryUuid) {

        /* 1.获取商品分类model*/
        Store_goodsclass pcModel = storeGoodsclassService.getByCategoryUuid(categoryUuid);

        /* 2.转换为DTO并返回*/
        ProductCategoryDTO p=new ProductCategoryDTO();
        if(!Lang.isEmpty(pcModel)){
            p.setCategoryUuid(pcModel.getId());
            p.setCategoryName(pcModel.getName());
            p.setCategoryNo("");
            p.setCategoryUrl("");
            p.setPosition(pcModel.getLocation());
            p.setParentUuid(pcModel.getParentId());
        }
        return p;
    }



    //去查询
    public Pagination doSearch(String storeUuid, String keyWords,String categoryUuid, int pageShow, int nowPage,  String sortBy, String sortType, String startPrice, String endPrice){
        String classId =categoryUuid ;
        String title ="" ;
        String keyword = keyWords;
        String sortName = sortBy;
        String sortOrder =sortType;
        if(sortOrder !=null && sortOrder !=""){
            if(sortOrder.equals("1")){
                sortOrder="asc";
            }else if(sortOrder.equals("2")){
                sortOrder="desc";
            }
        }
        if(sortName !=null && sortName !=""){
            if(sortName.equals("price")){
                sortName="productList.salePrice";
            }else if(sortName.equals("mount")){
                sortName="productList.saleNumAll";
            }
        }
        int startPri=0;
        int endPri=0;
        if(!Strings.isEmpty(startPrice)){
            startPri=Integer.parseInt(startPrice);
        }
        if(!Strings.isEmpty(endPrice)){
            endPri=Integer.parseInt(endPrice);
        }
        if(nowPage==0){
            nowPage=1;
        }
        boolean explain = true;
        Pagination page = new Pagination();
        page.setPageNo(nowPage);
        page.setPageSize(pageShow);
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if(Strings.isNotBlank(storeUuid)) {
            Store_main storeModel = storeMainService.fetch(storeUuid);
            if (storeModel != null) {
                query.must(QueryBuilders.matchPhraseQuery("storeId", storeModel.getId()));
            }
        }
        //价格查询
        if(startPri >0 && endPri >0){
            query.filter(QueryBuilders.rangeQuery("productList.salePrice").from(startPri).to(endPri));
        }else if(startPri >0){
            query.filter(QueryBuilders.rangeQuery("productList.salePrice").from(startPri));
        }else if(endPri>0){
            query.filter(QueryBuilders.rangeQuery("productList.salePrice").from(0).to(endPri));
        }
        //商品分类（包含子分类）
        if (Strings.isNotBlank(classId)) {
            String path = storeGoodsclassService.fetch(classId).getPath();
            query.must(QueryBuilders.wildcardQuery("storeGoodsClassList.path",path+"*"));
        }
        //根据商品名称查询
        if (Strings.isNotBlank(title)) {
            String[] keys = StringUtils.split(title, ' ');
            BoolQueryBuilder titleQuery = QueryBuilders.boolQuery();
            for (String key : keys) {
                if (Strings.isNotBlank(key)) {
                    titleQuery.should(QueryBuilders.wildcardQuery("name", "*" + key.toLowerCase() + "*"));
                    titleQuery.should(QueryBuilders.wildcardQuery("title", "*" + key.toLowerCase() + "*"));
                    titleQuery.should(QueryBuilders.wildcardQuery("spec", "*" + key.toLowerCase() + "*"));
                }
            }
            query.must(titleQuery);
        }
        //根据关键词查询
       if (Strings.isNotBlank(keyword)) {
           char [] keys = keyword.trim().toCharArray();
            BoolQueryBuilder keywordQuery = QueryBuilders.boolQuery();
            for (char cKey : keys) {
                String key=String.valueOf(cKey);
                keywordQuery.should(QueryBuilders.wildcardQuery("name", "*" + key.toLowerCase() + "*"));
                keywordQuery.should(QueryBuilders.matchPhraseQuery("name", key.toLowerCase()));
                keywordQuery.should(QueryBuilders.matchPhraseQuery("title",  key.toLowerCase()));
                keywordQuery.should(QueryBuilders.matchPhraseQuery("spec",  key.toLowerCase()));
                keywordQuery.should(QueryBuilders.matchPhraseQuery("keyWords",key.toLowerCase()));

            }
            query.must(keywordQuery);
        }
        //没有删除的商品
        query.must(QueryBuilders.termQuery("delFlag", false));
        //是上架商品
        query.must(QueryBuilders.termQuery("sale", true));
      /*  query.must(QueryBuilders.termsQuery("productList.id", "2017060000000003"));*/
        log.debug("query:::\r\n" + query.toString());
        System.out.println("query:::\r\n" + query.toString());
        SearchRequestBuilder srb = esService.getClient().prepareSearch(config.get("es.index.name"))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setTypes("goods")
                .setQuery(query)
                //分页
                .setFrom((nowPage - 1) * pageShow).setSize(pageShow)
                //是否高亮
                .setExplain(explain);
        if (explain) {
            HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
            highlightBuilder.preTags("<span style=\"color:red\">");
            highlightBuilder.postTags("</span>");
            srb.highlighter(highlightBuilder);
        }
        if (Strings.isNotBlank(sortName)) {
            if ("asc".equalsIgnoreCase(sortOrder)) {
                srb.addSort(sortName, SortOrder.ASC);
            } else
                srb.addSort(sortName, SortOrder.DESC);
        }
        SearchResponse response = srb.execute().actionGet();
        SearchHits hits = response.getHits();
        page.setTotalCount((int) hits.getTotalHits());
        List<Goods_main> list = new ArrayList<>();
        for (SearchHit searchHit : hits) {
            Map<String, Object> source = searchHit.getSource();
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            //name高亮
            HighlightField nameField = highlightFields.get("name");
            if (nameField != null) {
                nameField.fragments();
               Text[] fragments = nameField.fragments();
                String tmp = "";
                for (Text text : fragments) {
                    tmp += text;
                }
                source.put("name", tmp);
            }
            //name高亮
            HighlightField titleField = highlightFields.get("title");
            if (titleField != null) {
                Text[] fragments = titleField.fragments();
                String tmp = "";
                for (Text text : fragments) {
                    tmp += text;
                }
                source.put("title", tmp);
            }
            list.add(Lang.map2Object(source,Goods_main.class));
        }
        page.setList(list);
        return page;
    }

    /**
     * 获取商品的主信息
     */
    public ProductDetailInteractiveDTO getProductMain(String sku){
        ProductDetailInteractiveDTO productMainDTO = null;
        QueryBuilder query = QueryBuilders.matchQuery("productList.sku",sku);
        SearchRequestBuilder srb = esService.getClient().prepareSearch(config.get("es.index.name"))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setTypes("goods")
                .setQuery(query);
        SearchResponse response = srb.execute().actionGet();
        SearchHits hits = response.getHits();
        if(hits.getTotalHits() == 1){
            productMainDTO = new ProductDetailInteractiveDTO();
            Map<String, Object> source = hits.getHits()[0].getSource();
            Goods_main goodsMain = Lang.map2Object(source,Goods_main.class);
            //获取默认货品的信息
            List<Goods_product> productList = goodsMain.getProductList();
            if(productList != null){
                List<Goods_product> enabledProductList = new ArrayList<>();
                for(Goods_product goodsProduct :productList) {
                    if (goodsProduct.isEnabled()) {
                        enabledProductList.add(goodsProduct);
                    }
                    if(sku.equals(goodsProduct.getSku())) {
                        productMainDTO.setProductUuid(goodsProduct.getId());
                        //获取并设置商品名称
                        productMainDTO.setProductName(goodsProduct.getName());
                        productMainDTO.setSkuNo(goodsProduct.getSku());
                        //获取并设置商品的市场价
                        productMainDTO.setMarketPrice(goodsProduct.getMarketPrice());
                        //获取商品的商城价格--基础价格--最终价格取决于促销活动
                        productMainDTO.setShopPrice(goodsProduct.getSalePrice());
                        //设置最低购买积分
                        productMainDTO.setLeastIntegral(0);
                        productMainDTO.setStock(goodsProduct.getStock()-goodsProduct.getStockFreeze());
                        String spec=goodsProduct.getSpec();
                        String att="";
                        ProductAttrStockDTO attrStock =null;
                        if(!Strings.isEmpty(spec)){
                            Object obj1=Json.fromJson(spec);
                            List attrLists=(List)obj1;
                            if(attrLists !=null && attrLists.size()>0){
                                for(int b=0;b<attrLists.size();b++){
                                    Map first=(Map)attrLists.get(b);
                                    att +="-"+first.get("spec_value_id");
                                }
                            }
                        }
                        productMainDTO.setAttrValueId(att);
                    }
                }
                goodsMain.setGoodsProducts(enabledProductList);
            }
            //获取PC端图片
            List<Goods_image> goodsImageList = goodsMain.getImageList();
            //过滤出PC端的图片
            GoodsUtil.getClientGoodsImage(goodsImageList,GoodsSaleClientEnum.PC.getKey());
          //  req.setAttribute("obj",goodsMain);
            if (goodsImageList != null && goodsImageList.size()>0) {
                productMainDTO.setBigImageUrl(goodsProductService.getProductImage(sku));
                productMainDTO.setCenterImageUrl(goodsProductService.getProductImage(sku));
                productMainDTO.setSmallImageUrl(goodsProductService.getProductImage(sku));
            }
            productMainDTO.setProductMultiImage(convertProductMainMutiImageDTO(goodsImageList));
            String storeId=goodsMain.getStoreId();
            Store_main storeMain=storeMainService.fetch(storeId);
            //设置是否自营
            if(storeMain !=null){
                if(storeMain.isSelf()){
                    productMainDTO.setSelfSupport("0");
                }else{
                    productMainDTO.setSelfSupport("1");
                }
            }

            //获取促销信息
            List<Sales_rule_goods> saleRuleGoods=salesRuleGoodsService.sales(goodsMain.getStoreId(),sku, null,null);
            List<String> promotionNames=new ArrayList<>();
            if(saleRuleGoods !=null && saleRuleGoods.size()>0){
                for(Sales_rule_goods saleRuleGood:saleRuleGoods){
                    String promotionName=saleRuleGood.getName();
                    promotionNames.add(promotionName);
                }
            }
            productMainDTO.setPromotionName(promotionNames);
            //设置商品评价语
            productMainDTO.setAdviceNote(goodsMain.getTitle());
            //设置商品的评论数以及评论量 [有缓存]
            int feedBackNum =  orderGoodsFeedbackService.count(Cnd.where("delFlag","=",false).and("sku","=",sku));
            productMainDTO.setAppCount(feedBackNum);
            List<ChoosedAttributeInValueJson> list1 = new ArrayList<>();
            //如果是规格商品
            if (goodsMain.isHasSpec()) {
                //设置是否是规格商品
                 productMainDTO.setSpec("0");
                //获取设置商品  可以选择的规格属性
                String spec=goodsMain.getSpec();
                if(!Strings.isEmpty(spec)){
                    Object obj=Json.fromJson(spec);
                    List list=(List) obj;
                    if(list !=null && list.size()>0){
                        for(int x=0;x<list.size();x++){
                            List<AttributeValue> values=new ArrayList<AttributeValue>();
                            ChoosedAttributeInValueJson jsonChoose=new ChoosedAttributeInValueJson();
                            Object sz=list.get(x);
                            List lists=(List)list.get(x);
                            Map first=(Map)lists.get(0);
                            jsonChoose.setAttributeName((String)first.get("spec_name"));
                            jsonChoose.setAttributeUuid((String)first.get("spec_value_id"));
                            if(lists !=null && lists.size()>0){
                                for(int z=0;z<lists.size();z++){
                                    Map map=(Map)lists.get(z);
                                    AttributeValue attributeValue=new AttributeValue();
                                    attributeValue.setValue( (String) map.get("spec_value_name"));
                                    attributeValue.setValueUuid((String) map.get("spec_value_id"));
                                    values.add(attributeValue);
                                }
                            }
                            jsonChoose.setValues(values);
                            list1.add(jsonChoose);
                        }
                    }
                    productMainDTO.setSelectAttrValues(convertChoosedAttrValue(list1));
                }

            }
        }






















   /*     String customerUuid = StringUtil.getMemberUid();
        //ProductDetailInteractiveDTO productMainDTO = null;
       *//* String[] skuArr=new String[1];
        skuArr[0]=sku;
        List<Goods_main> mains=getProductList(skuArr);*//*

        Goods_product product = goodsProductService.fetch(Cnd.where("sku","=",sku));
        Goods_main mainModel=goodsService.fetch(product.getGoodsId());
        if(product == null){
            throw new ProductException("没找到该sku对应的商品");
        }

        ////过滤出PC端的图片
        //获取PC端图片
        List<Goods_image> images= goodsImageService.query(Cnd.where("goodsId","=",mainModel.getId()));
        //过滤出PC端的图片
        GoodsUtil.getClientGoodsImage(images,GoodsSaleClientEnum.PC.getKey());
        if (images != null && images.size()>0) {
            productMainDTO.setBigImageUrl(goodsProductService.getProductImage(sku));
            productMainDTO.setCenterImageUrl(goodsProductService.getProductImage(sku));
            productMainDTO.setSmallImageUrl(goodsProductService.getProductImage(sku));
        }
        //获取并设置商品其他多角度试图
        //List<ProductMainMultiImageModel> productMultiImage = product.getProductMultiImage();
        productMainDTO.setProductMultiImage(convertProductMainMutiImageDTO(images));
        String storeId=product.getStoreId();
        Store_main storeMain=storeMainService.fetch(storeId);
        //设置是否自营
        if(storeMain !=null){
            if(storeMain.isSelf()){
                productMainDTO.setSelfSupport("0");
            }else{
                productMainDTO.setSelfSupport("1");
            }
        }
        //获取并设置商品名称
        productMainDTO.setProductName(product.getName());
        //设置商品评价语
        productMainDTO.setAdviceNote(mainModel.getTitle());
        //获取并设置商品的市场价
        productMainDTO.setMarketPrice(product.getMarketPrice());
        //获取商品的商城价格--基础价格--最终价格取决于促销活动
        productMainDTO.setShopPrice(product.getSalePrice());
        //设置最低购买积分
        productMainDTO.setLeastIntegral(0);
        //StringUtil.getImg("pc","alumb")
        //设置商品的评论数以及评论量 [有缓存]
        int feedBackNum =  orderGoodsFeedbackService.count(Cnd.where("delFlag","=",false).and("sku","=",sku));
        productMainDTO.setAppCount(feedBackNum);

        //获取设置商品的 单品促销信息
        double shopPrice =product.getSalePrice();
        //获取并设置商品库存信息-默认选择的sku以及库存[有缓存]
       *//*ProductAttrStockModel productAttrStock = productStockService.getByDefault(productUuid);
       productMainDTO.setAttrStock(convertAttrStockDTO(productAttrStock));*//*
        productMainDTO.setSkuNo(product.getSku());
        productMainDTO.setStock(product.getStock()-product.getStockFreeze());
        //促销这一块暂时没有。后期添加

        //获取商品的促销信息和最终促销价格
        *//*ProductPriceAndPromotionModel promotions = frontProductOrdinaryService.getPromotionModels(productUuid, "", shopPrice, productMain.getStoreUuid());
        if(promotions != null){
            productMainDTO.setShopPrice(promotions.getPrice());
            productMainDTO.setProductPromotions(convertProductPromotions(promotions.getPromotions()));
            productMainDTO.setPromotionName(promotions.getName());
        }
        //获取设置 商品的 店铺促销
        List<PromotionModel> storePromotions = frontProductOrdinaryService.getStorePromotions(productMain.getStoreUuid(), customerUuid,productUuid);
        if(storePromotions!=null && storePromotions.size()>0){
            productMainDTO.setStorePromotions(convertProductPromotions(storePromotions));
        }*//*


        //设置是否是规格商品
        productMainDTO.setSpec(product.getSpec());
        List<ChoosedAttributeInValueJson> list1 = new ArrayList<>();
        //如果是规格商品
        if (mainModel.isHasSpec()) {
            //获取设置商品  可以选择的规格属性
            String spec=mainModel.getSpec();
            if(!Strings.isEmpty(spec)){
                Object obj=Json.fromJson(spec);
                List list=(List) obj;
                if(list !=null && list.size()>0){
                    for(int x=0;x<list.size();x++){
                        List<AttributeValue> values=new ArrayList<AttributeValue>();
                        ChoosedAttributeInValueJson jsonChoose=new ChoosedAttributeInValueJson();
                        Object sz=list.get(x);
                        List lists=(List)list.get(x);
                        Map first=(Map)lists.get(0);
                        jsonChoose.setAttributeName((String)first.get("spec_name"));
                        jsonChoose.setAttributeUuid((String)first.get("spec_value_id"));
                        if(lists !=null && lists.size()>0){
                            for(int z=0;z<lists.size();z++){
                                Map map=(Map)lists.get(z);
                                AttributeValue attributeValue=new AttributeValue();
                                attributeValue.setValue( (String) map.get("spec_value_name"));
                                attributeValue.setValueUuid((String) map.get("spec_value_id"));
                                values.add(attributeValue);
                            }
                        }
                        jsonChoose.setValues(values);
                        list1.add(jsonChoose);
                    }
                }
                productMainDTO.setSelectAttrValues(convertChoosedAttrValue(list1));
            }

            //设置颜色图片
           *//* List<ProductColorImageModel> colorImages =  product.getProductColorImages();
            if(colorImages!=null && colorImages.size() >0 ){
                productMainDTO.setProductColorImage(convertProductColorImageDTO(colorImages));
            }*//*
        }

        //获取并设置商品销量[有缓存] --- 浏览足迹需要使用
        int count =  goodsFavoriteService.count(Cnd.where("delFlag","=",false).and("sku","=",product.getSku()));
        productMainDTO.setFavoriteCount(count);
        productMainDTO.setSaleCount(product.getSaleNumAll());*/
        return productMainDTO;
    }


    /**
     *
     * @param selectedAttributeInValueJsons
     * @return
     */
    private List<SelectedAttributeInValueJsonDTO>  convertChoosedAttrValue(List<ChoosedAttributeInValueJson> selectedAttributeInValueJsons){
        List<SelectedAttributeInValueJsonDTO> selectedAttributeInValueJsonDTOs = new ArrayList<>();
        if(null != selectedAttributeInValueJsons && !selectedAttributeInValueJsons.isEmpty()){
            for(ChoosedAttributeInValueJson model : selectedAttributeInValueJsons){
                List<AttributeValueDTO> attributeValueDTOs = convertAttributeValue(model.getValues());
                SelectedAttributeInValueJsonDTO   dto = new SelectedAttributeInValueJsonDTO(model.getAttributeUuid(), model.getAttributeName(), model.getCanColor(),
                        attributeValueDTOs);
                selectedAttributeInValueJsonDTOs.add(dto);
            }
        }
        return selectedAttributeInValueJsonDTOs;
    }
    public  List<AttributeValueDTO> convertAttributeValue(List<AttributeValue> attributeValues){
        List<AttributeValueDTO> attributeValueDTOs = new ArrayList<>();
        AttributeValueDTO attributeValueDTO;
        if(null !=attributeValues && !attributeValues.isEmpty()){
            for(AttributeValue model : attributeValues){
                attributeValueDTO = new AttributeValueDTO(model.getValueUuid(), model.getValue());
                attributeValueDTOs.add(attributeValueDTO);
            }
        }
        return attributeValueDTOs;
    }

    /**
     * 根据商品UUID获取商品评价评分
     * @param sku 商品sku
     * @return 返回评分
     */
    public double getProductAvgScore(String sku){
    	/* 商品评分展示 */
        double productScore = 0.00;
     /*   if (!Strings.isEmpty(productUuid)) {
            productScore = productAppService.getAverageScore(productUuid);
        }*/
        if (BigDecimal.valueOf(productScore).compareTo(new BigDecimal(0)) == 0) {
            productScore = 5.0;
        }
        return productScore;
    }
    /**
     * 转换多视角图片对象
     * @param productMultiImage
     * @return
     */
    private List<ProductMainMultiImageDTO> convertProductMainMutiImageDTO( List<Goods_image> productMultiImage){
        List<ProductMainMultiImageDTO> productMultiImageDTOs = new ArrayList<>();
        ProductMainMultiImageDTO dto;
        if(null != productMultiImage && !productMultiImage.isEmpty()){
            for(Goods_image model : productMultiImage){
                dto = new ProductMainMultiImageDTO(model.getId(), model.getGoodsId(), model.getImgOriginal(),
                        model.getImgAlbum(),model.getImgAlbum(), model.getImgAlbum(), model.getLocation());
                productMultiImageDTOs.add(dto);
            }
        }
        return productMultiImageDTOs;
    }
    /**
     * 选择属性返回商品信息接口
     *
     * @return 返回选择的属性DTO
     *//*
    @Override
    public SpecChooseDTO selectSpec(String sku, String selectUuidList, String promotionUuid) {

        *//* 1.获取主商品信息 *//*
        Goods_product product = goodsProductService.fetch(Cnd.where("sku","=",sku));
        Goods_main mainModel=goodsService.fetch(product.getGoodsId());
       // com.aebiz.b2b2c.product.productpub.vo.ProductModel m = frontProductOrdinaryService.getByUuid(productUuid);
        //ProductMainModel productMain = m.getProductMain();

        *//* 2.获取商品库存及价格信息 *//*
        ProductAttrStockModel stockModel = productStockService.getByProductUuidAndSkuUuid(productUuid, selectUuidList);
        int stock=0;
        double price=0.0;
        String skuNo="";
        if (product != null) {

            *//* 2.1获取商品库存 *//*
            stock=product.getStock();
            skuNo=product.getSku();

            *//* 2.2获取商品最终价格 *//*
            ProductPriceAndPromotionModel promotions;
            if(!Strings.isEmpty(promotionUuid)){
                Map<String, Object> map = new HashMap<>();
                promotions = frontProductOrdinaryService.getPromotionModels(productUuid, "", stockModel.getPrice(),
                        productMain.getStoreUuid(),promotionUuid,map);
            }else{
                promotions = frontProductOrdinaryService.getPromotionModels(productUuid, "", stockModel.getPrice(), productMain.getStoreUuid());
            }
            if(promotions!=null){
                price=promotions.getPrice();
            }
        }

        *//* 3.获取商品图片信息 *//*
        String bigPictureUrl="",centerPictureUrl="",smallPictureUrl="";
        ProductMainImageModel productImage = m.getProductImage();
        if (productImage != null) {
            bigPictureUrl=productImage.getBigImageUrl();
            centerPictureUrl=productImage.getCenterImageUrl();
            smallPictureUrl=productImage.getSmallImageUrl();
        }
        String[] initSelectSpecUuid = selectUuidList.split("-");
        ProductColorImageModel productColor = colorImageService.getByProductUuidAndAttrValueUuid(productUuid, initSelectSpecUuid[0]);
        if (productColor != null) {
            bigPictureUrl=productColor.getBigImageUrl();
            centerPictureUrl=productColor.getCenterImageUrl();
            smallPictureUrl = productColor.getSmallImageUrl();
        }

        *//* 4.获取商品规格选择DTO *//*
        return  new SpecChooseDTO(bigPictureUrl, centerPictureUrl, smallPictureUrl, price, stock, skuNo, selectUuidList);

    }*/


        /**
        *获取一级分类和一级分类的子分类
         *
         */

    @Override
    public Map<String, Object> searchProductAndScreenAttr(Map<String,String> queryParam) {
        String categoryUuid = queryParam.get("categoryUuid");
        Pagination p= doSearch("", queryParam.get("keyword"),categoryUuid,Integer.parseInt(queryParam.get("pageShow")), Integer.parseInt(queryParam.get("nowPage")), "", "","", "");
        int totalNum = p.getTotalCount();
        List<Goods_main> mainList=(List<Goods_main>)p.getList();
        List<Store_goodsclass> allStoreList=new ArrayList<>();
        List<String>  strList=new ArrayList<>();
        if(mainList !=null && mainList.size()>0){
            for(Goods_main mainModel:mainList){
                List<Store_goodsclass> storeClasss=mainModel.getStoreGoodsClassList();
                if(storeClasss !=null && storeClasss.size()>0){
                    for(Store_goodsclass goodsclass:storeClasss){
                        if(Strings.isEmpty(goodsclass.getParentId())){
                            if(!Strings.isEmpty(categoryUuid)){
                                Store_goodsclass goodsclass1= storeGoodsclassService.fetch(categoryUuid);
                                if(!Lang.isEmpty(goodsclass1)){
                                    if(!strList.contains(goodsclass1.getId())){
                                        strList.add(goodsclass1.getId());
                                        Store_goodsclass goodsclass2= storeGoodsclassService.fetch(goodsclass.getParentId());
                                        List<Store_goodsclass> storeClasss4=  storeGoodsclassService.query(Cnd.where("parentId","=",goodsclass2.getId()));
                                        goodsclass2.setClassList( storeGoodsclassService.query(Cnd.where("parentId","=",goodsclass2.getId())));
                                        allStoreList.add(goodsclass2);
                                    }
                                }
                            }
                        }else{
                            Store_goodsclass goodsclass1= storeGoodsclassService.fetch(goodsclass.getParentId());
                            if(!Lang.isEmpty(goodsclass1)) {
                                if (!strList.contains(goodsclass1.getId())) {
                                    strList.add(goodsclass1.getId());
                                    Store_goodsclass goodsclass3 = storeGoodsclassService.fetch(goodsclass.getParentId());
                                    List<Store_goodsclass> storeClasss4 = storeGoodsclassService.query(Cnd.where("parentId", "=", goodsclass3.getId()));
                                    goodsclass3.setClassList(storeGoodsclassService.query(Cnd.where("parentId", "=", goodsclass3.getId())));
                                    allStoreList.add(goodsclass3);
                                }
                            }
                        }
                    }
                }
            }
        }
        // 获取前台分类子集分类集合
        List<Store_goodsclass> goodsclasses=storeGoodsclassService.getAllProductCategoryFront(categoryUuid);
        List<ProductCategoryDTO> cateList =null;
        if(!Strings.isEmpty(categoryUuid)){
            List<Store_goodsclass> secStoreClasss=storeGoodsclassService.getAllProductCategoryFront(categoryUuid);
            cateList=ProductConvertHelper.convertProductCategoryToDTOs(secStoreClasss);
        }else{
            cateList =ProductConvertHelper.convertProductCategoryToDTOs(allStoreList);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("subCate", cateList);
        return resultMap;
    }

    /**
     * 添加商品收藏
     * @param sku 商品uuid
     * @return 返回结果：收藏状态：1：收藏成功。2：收藏失败 3：已经收藏 ；收藏记录的uuid
     */
    @Override
    public String addProductFavorite(String sku) {

        /* 1.获取用户uuid */
        String customerUuid = StringUtil.getMemberUid();

        /* 2.返回收藏结果 */
        return goodsProductService.saveProductFavorite(customerUuid, sku);
    }

    /**
     * 获取商品收藏列表
     * @return 返回收藏列表
     */
    @Override
    public int getProductFavorite() {

        /* 1.设置查询条件 */
        String customerUuid = StringUtil.getMemberUid();
        int count=goodsFavoriteService.count(Cnd.where("accountId","=",customerUuid));
        /* 2.获取收藏model *//*
        Pagination p=goodsFavoriteService.listPage(Integer.parseInt(nowPage),Integer.parseInt(pageShow),Cnd.where("accountId","=",customerUuid).and("delFlag","=",false));
        List<Goods_favorite> list =(List<Goods_favorite>)p.getList();
        *//* 3.将model转化为DTO *//*
        List<ProductFavoriteDTO> productFavoriteDTOs = ProductConvertHelper.convertProductFavorite(list);

        *//* 4.返回收藏列表 *//*
        p.setList(productFavoriteDTOs);*/
        return count;
    }
    /**
     * 获取商品详情简介，规格与包装，售后服务信息
     * @param sku
     * @return
     */
    public ProductDescDTO getProductDetail(String sku){
        ProductDescDTO productDescDTO = null;
        Goods_product product=goodsProductService.fetch(Cnd.where("sku","=",sku));
        if(product !=null){
            Goods_main main=goodsService.fetch(product.getGoodsId());
            productDescDTO = new ProductDescDTO();
            productDescDTO.setProductUuid(sku);
            productDescDTO.setDescription(main.getPcNote());
            //设置商品规格与包装
            productDescDTO.setProdAttrs(main.getSpec());
            //设置商品售后服务
            String typeId=main.getTypeId();
            Goods_type_tab tabs=goodsTypeTabService.fetch(Cnd.where("typeId","=",typeId));
             String tabNote="";
            if(tabs !=null){
                tabNote=tabs.getNote();
            }
            productDescDTO.setSaleAfterNote(tabNote);
        }
        return productDescDTO;
    }

    /**
     * 根据商品uuid查询各种评价的数量
     * @param sku 商品uuid
     * @return 商品评价DTO
     */
    @Override
    public ProductAppraiseScoreDTO getAppraiseCount(String sku) {
        // 总评价数
        int allCount = orderGoodsFeedbackService.getFeedbackNumBySku(sku);
        // 调取接口方法获得好评数
        int goodAppraiseCount = orderGoodsFeedbackService.getAppCountByType(sku, "1");
        // 调取接口方法获得中评数
        int middleAppraiseCount = orderGoodsFeedbackService.getAppCountByType(sku, "2");
        // 调取接口方法获得差评数
        int badAppraiseCount = orderGoodsFeedbackService.getAppCountByType(sku, "3");
        // 晒图评论数量
        int picCount=orderGoodsFeedbackService.getAppCountByType(sku, "4");

        return new ProductAppraiseScoreDTO(allCount, goodAppraiseCount, middleAppraiseCount, badAppraiseCount, picCount);
    }

    /**
     * 获取商品评价信息接口
     *
     * @param sku 商品sku
     * @param type 类型
     * @param nowPage 当前页面
     * @param pageShow 显示条数
     * @return 返回评价信息
     */

    public Pagination getCommentsNew(String sku, String type, int nowPage, int pageShow) {
        if(type == null){
            type  = "0";
        }
       // CompWebModel wm=new CompWebModel();
        int totalNum=500;
        /*wm.setTotalNum(totalNum);
        wm.setNowPage(nowPage);
        wm.setPageShow(pageShow);*/

        List<ProductAppraiseContentDTO> productAppraiseContentDTOList = new ArrayList<>();
        ProductAppraiseContentDTO productAppraiseContentDTO;

        /* 1.获取商品评价model列表 */
        Pagination p = orderGoodsFeedbackService.getAppByType(sku, type, nowPage, pageShow);
        totalNum= orderGoodsFeedbackService.getAppCountByType(sku, type);
        List<Order_goods_feedback> feedbacks =null;
        if(p !=null){
            feedbacks=(List<Order_goods_feedback>)p.getList();
        }
        //获取所有的晒单信息
        /* 2.循环列表内容，将model转化为DTO */
        if(feedbacks !=null && feedbacks.size()>0){
            List models=p.getList();
            for (int i=0;i< models.size();i++) {
                Record model =(Record)models.get(i);
                List<String> picList=new ArrayList<>();

            /* 2.1 获取并设置评分*/
                int score =(int) model.get("appScore");

            /* 2.2 获取并设置评价时间 */
                int appraiseT =(int) model.get("feedAt");
                long appraiseTime=(long)appraiseT*1000;
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = dateformat.format(appraiseTime);
            /* 2.3 获取并设置评论内容 */
                String appraiseContent = (String)model.get("feedNote");
              /* 2.4 获取并设置晒单图片信息 */
                String freedImage=(String)model.get("feedImage");
                if(!Strings.isEmpty(freedImage)){
                    String[] freedImg=freedImage.split(",");
                    picList = Arrays.asList(freedImg);
                }

            /* 2.5 获取并设置商品sku*/
            /* 2.6获取并设置用户头像及名称 */
                String customerImg ,customerName;
                Account_info info=accountInfoService.fetch((String)model.get("accountId"));
                if (info != null) {
                    customerImg = info.getImageUrl();
                    customerName = info.getNickname();
                }else{
                    customerImg = "";
                    customerName = "匿名用户";
                }
                productAppraiseContentDTO = new ProductAppraiseContentDTO(score, customerImg, customerName, dateStr,
                        appraiseContent, sku, picList);
                productAppraiseContentDTO.setSpec(this.getSpec(sku));
                productAppraiseContentDTOList.add(productAppraiseContentDTO);
            }
        }
        p.setList(productAppraiseContentDTOList);
        return p;

    }

    /**
     * 通过所买商品属性和属性值uuid，查询所买商品的属性，属性值集合
     * @return 返回属性值集合
     */
    private List<Datatable> getSpec(String sku) {
        if(!Strings.isEmpty(sku)){
            Goods_product product= goodsProductService.fetch(Cnd.where("sku","=",sku));
            List<Datatable> specList = new ArrayList<>();
            if(product!=null){
                Goods_main main=goodsService.fetch(product.getGoodsId());
                String str =  product.getSpec();
                if(!Strings.isEmpty(str)){
                    Object obj=Json.fromJsonAsList(List.class,str);
                    List specs=(List)obj;
                    if(!Lang.isEmpty(specs)){
                        for (Object obj1:specs) {
                            List map=(List)obj1;
                            Datatable dataTablesPageParam=new Datatable();
                            dataTablesPageParam.setName(String.valueOf(map.get(0)));
                            dataTablesPageParam.setValue(String.valueOf(map.get(4)));
                            specList.add(dataTablesPageParam);
                        }
                    }
                }
            }
            return specList;
        }
        return null;
    }
    /**
     * 获取商品详情信息
     * @param sku 商品uuid
     * @return 返回商品详细信息
     */
    @Override
    public CompProductDetailDTO getProductDetailInfo(String sku) {
        CompProductDetailDTO compProductDetailDTO = new CompProductDetailDTO();
        ProductDetailDTO productDetailDTO;
        Goods_product product = goodsProductService.fetch(Cnd.where("sku","=",sku));
        Goods_main main = goodsService.fetch(product.getGoodsId());
        String img=goodsProductService.getProductImage(sku,GoodsSaleClientEnum.PC);
        //设置商品售后服务
        String typeId=main.getTypeId();
        String tabNote="";
        if(!Strings.isEmpty(typeId)){
            Goods_type_tab tabs=goodsTypeTabService.fetch(Cnd.where("typeId","=",typeId));
            if(tabs !=null){
                tabNote=tabs.getNote();
            }
        }
        List<ChoosedAttributeInValueJson> jsonModels = null;
        //获取商户
        String storeId=main.getStoreId();
        String storeName="";
        String storeLogo="";
        if(!Strings.isEmpty(storeId)){
            Store_main storeMain=storeMainService.fetch(storeId);
            storeName=storeMain.getStoreName();
            storeLogo=storeMain.getLogo();
        }
        //获取分类
        String classId=main.getClassId();
        String goodsclassName="";
        if(!Strings.isEmpty(classId)){
            Store_goodsclass goodsclass =storeGoodsclassService.fetch(classId);
            goodsclassName=goodsclass.getName();
        }
        //获取品牌
        String brandId=main.getBrandId();
        String brandName="";
        if(!Strings.isEmpty(brandId)){
            Goods_brand brand=goodsBrandService.fetch(brandId);
            brandName=brand.getName();
        }

        if(main!=null){
            if(main.isHasSpec()){
                String spec = main.getSpec();
                if(!Strings.isEmpty(spec)){
                    Object obj=Json.fromJson(spec);
                    List list=(List) obj;
                    if(list !=null && list.size()>0){
                        for(int x=0;x<list.size();x++){
                            List<AttributeValue> values=new ArrayList<AttributeValue>();
                            ChoosedAttributeInValueJson jsonChoose=new ChoosedAttributeInValueJson();
                            Object sz=list.get(x);
                            List lists=(List)list.get(x);
                            Map first=(Map)lists.get(0);
                            jsonChoose.setAttributeName((String)first.get("spec_name"));
                            jsonChoose.setAttributeUuid((String)first.get("spec_value_id"));
                            if(lists !=null && lists.size()>0){
                                for(int z=0;z<lists.size();z++){
                                    Map map=(Map)lists.get(z);
                                    AttributeValue attributeValue=new AttributeValue();
                                    attributeValue.setValue( (String) map.get("spec_value_name"));
                                    attributeValue.setValueUuid((String) map.get("spec_value_id"));
                                    values.add(attributeValue);
                                }
                            }
                            jsonChoose.setValues(values);
                            jsonModels.add(jsonChoose);
                        }
                    }
                }
            }
            List<SelectedAttributeInValueJsonDTO> selectedAttributeInValueJsonDTOs =convertChoosedAttrValue(jsonModels);
            productDetailDTO = new ProductDetailDTO();
            productDetailDTO.setProductUuid(product.getId());
            productDetailDTO.setProductName(main.getName());
            productDetailDTO.setAdviceNote(main.getTitle());
            productDetailDTO.setCategoryUuid(main.getClassId());
            productDetailDTO.setCategoryName(goodsclassName);
            productDetailDTO.setStoreName(storeName);
            productDetailDTO.setStoreUuid(main.getStoreId());
            productDetailDTO.setStoreLogo(storeLogo);
            productDetailDTO.setBrandUuid(main.getBrandId());
            productDetailDTO.setBrandName(brandName);
            productDetailDTO.setMarketPrice(product.getMarketPrice());
            productDetailDTO.setShopPrice(product.getSalePrice());
            productDetailDTO.setActualStock(product.getStock());
            productDetailDTO.setMainImageUrl(img);
            /*productDetailDTO.setBigImageUrl(imageModel.getBigImageUrl());
            productDetailDTO.setCenterImageUrl(imageModel.getCenterImageUrl());
            productDetailDTO.setSmallImageUrl(imageModel.getSmallImageUrl());
            productDetailDTO.setPopImage1Url(imageModel.getPopImage1Url());
            productDetailDTO.setPopImage2Url(imageModel.getPopImage2Url());
            productDetailDTO.setPopImage3Url(imageModel.getPopImage3Url());
            productDetailDTO.setPopImage4Url(imageModel.getPopImage4Url());
            productDetailDTO.setPopImage5Url(imageModel.getPopImage5Url());
            productDetailDTO.setPopImage6Url(imageModel.getPopImage6Url());
            productDetailDTO.setPopImage7Url(imageModel.getPopImage7Url());*/
            productDetailDTO.setUseDefult("1");
            productDetailDTO.setSaleAfterNote(tabNote);
            productDetailDTO.setDescription(main.getPcNote());
            productDetailDTO.setNote(main.getPcNote());
            productDetailDTO.setProdAttrs(main.getSpec());

            compProductDetailDTO = new CompProductDetailDTO(productDetailDTO, selectedAttributeInValueJsonDTOs);
        }
        return compProductDetailDTO;
    }

    /**
     * 通过商品sku获取店铺uuid
     * @param sku
     * @return
     */
    @Override
    public String getStoreUuidByProductUuid(String sku) {
        Goods_product product=goodsProductService.fetch(Cnd.where("sku","=",sku));
        if(product !=null){
           String goodsId=product.getGoodsId();
           Goods_main main=goodsService.fetch(goodsId);
           String storeId=main.getStoreId();
           return  storeId;
        }
        return "";
    }


    /**
     * 根据商户查询商户某一分类对应的同级分类
     */
    @Override
    public List<ProductCategoryDTO> getSameLevelProductCategorysByStoreUuid(String storeUuid) {
        List<ProductCategoryDTO> retList = new ArrayList<ProductCategoryDTO>();

        List<Store_goodsclass> pcsmList = storeGoodsclassService.getSameLevelProductCategorysByStoreUuid(storeUuid);
        if(pcsmList==null || pcsmList.size()<=0) {
            return retList;
        }
        for(Store_goodsclass model : pcsmList){
            ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
            String parentId=model.getParentId();
            Store_goodsclass storeGoodsclass=storeGoodsclassService.fetch(parentId);
            if(storeGoodsclass !=null){
                productCategoryDTO.setParentUuid(storeGoodsclass.getParentId());
                productCategoryDTO.setCategoryUuid(storeGoodsclass.getId());
                productCategoryDTO.setPosition(storeGoodsclass.getLocation());
                productCategoryDTO.setCategoryName(storeGoodsclass.getName());
                List<Store_goodsclass> store_goodsclasses=storeGoodsclassService.query(Cnd.where("parentId","=",storeGoodsclass.getId()).and("storeId","=",storeGoodsclass.getStoreId()));
                List<ProductCategoryDTO> productCategoryDTOs=new ArrayList<>();
                if(store_goodsclasses !=null && store_goodsclasses.size()>0){
                    for(Store_goodsclass model2 : store_goodsclasses){
                        ProductCategoryDTO productCategoryDTO2 = new ProductCategoryDTO();
                        productCategoryDTO2.setParentUuid(model2.getParentId());
                        productCategoryDTO2.setCategoryUuid(model2.getId());
                        productCategoryDTO2.setPosition(model2.getLocation());
                        productCategoryDTO2.setCategoryName(model2.getName());
                        productCategoryDTOs.add(productCategoryDTO2);
                    }

                }
                productCategoryDTO.setSubCategoryList(productCategoryDTOs);
            }
            retList.add(productCategoryDTO);
        }
        return retList;
    }

    /**
     * 选择属性返回商品信息接口
     *
     * @param selectUuidList 选择的属性列表
     * @return 返回选择的属性DTO
     */
    @Override
    public SpecChooseDTO selectSpec(String sku, String selectUuidList) {
        String customerUuid = StringUtil.getMemberUid();
        String[] selectUuidArr=selectUuidList.split("-");

        String getSku=goodsProductService.getSkuBySpecs(sku,selectUuidArr);
        /* 1.获取主商品信息 */
        Goods_product product = goodsProductService.fetch(Cnd.where("sku","=",getSku));
        Goods_main mainModel=goodsService.fetch(product.getGoodsId());
        double price=0.0;
        if(Strings.isEmpty(customerUuid)){
            price=product.getSalePrice();
        }else{
            price=goodsPriceService.getSalePrice(getSku,null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null);
        }
        if(product == null){
            throw new ProductException("没找到该sku对应的商品");
        }
        /* 2.1获取商品库存 */
        int stock=product.getStock();
        String skuNo=product.getSku();

        /* 3.获取商品图片信息 */
        String bigPictureUrl="",centerPictureUrl="",smallPictureUrl="";
        ////过滤出PC端的图片
        //获取PC端图片
        List<Goods_image> images= goodsImageService.query(Cnd.where("goodsId","=",mainModel.getId()));

        //过滤出PC端的图片
        GoodsUtil.getClientGoodsImage(images,GoodsSaleClientEnum.PC.getKey());
        if (images != null && images.size()>0) {
            bigPictureUrl=goodsProductService.getProductImage(getSku);
            centerPictureUrl=goodsProductService.getProductImage(getSku);
            smallPictureUrl=goodsProductService.getProductImage(getSku);
        }
        /* 4.获取商品规格选择DTO */
        return  new SpecChooseDTO(product.getName(),bigPictureUrl, centerPictureUrl, smallPictureUrl, price, stock, skuNo, selectUuidList);

    }
    /**
     * 根据规格数组判断不能点的规格（详情）
     */
    public List<String> getSpecDisable(String sku,String[] spec){
        if(!Lang.isEmpty(spec)){
            for(int i=0;i<spec.length;i++){

            }
        }
        return null;
    }

}
