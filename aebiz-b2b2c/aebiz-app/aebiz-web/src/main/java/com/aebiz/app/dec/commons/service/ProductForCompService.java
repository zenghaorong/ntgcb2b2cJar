package com.aebiz.app.dec.commons.service;

import com.aebiz.app.web.modules.controllers.open.dec.dto.product.*;
import com.aebiz.baseframework.page.Pagination;

import java.util.List;
import java.util.Map;

/**
 * 商品相关业务
 *
 * Created by Aebiz_yjq on 2016/12/14.
 */
public interface ProductForCompService {
    /**
     * 获取联想词
     *
     * @param keyWord 请求参数：关键词
     * @return  返回结果：联想词列表
     */
    List<String> getLikeKey(String keyWord);

    /**
     * 获取所有的前台分类(取前三级)
     *
     * @return 前台分类列表
     */
    public  List<ProductCategoryDTO> getFrontCategory();

    /**
     * 获取平台一级分类
     *
     * @return 返回平台一级分类
     */
    List<ProductCategoryDTO> getFirstLevelCategory();

    /**
     * 批量获取商品简单信息
     * @param arr
     * @return
     */

    List<ProductDetailDTO> getBatchProductSample(String[] arr);
    /**
     * 根据商品分类uuid获取分类
     *
     * @param categoryUuid 商品分类uuid
     * @return 商品分类dto
     */
    ProductCategoryDTO getProductCateByUuid(String categoryUuid);
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
    public Pagination search(String storeUuid, String keyWords, String categoryUuid, int pageShow, int nowPage, String sortBy, String sortType, String startPrice, String endPrice);

    public Pagination doSearch(String storeUuid, String keyWords, String categoryUuid, int pageShow, int nowPage, String sortBy, String sortType, String startPrice, String endPrice);
    /**
     * 获取商品主要信息
     * @param sku
     * @return
     */
    ProductDetailInteractiveDTO getProductMain(String sku);
    /**
     * 根据商品UUID获取商品评价评分
     * @param sku 商品sku
     * @return 返回评分
     */
    public double getProductAvgScore(String sku);

    /**
     * 选择属性返回商品信息接口
     *
     * @param productUuid 商品uuid
     * @param selectUuidList 选择的属性列表
     * @param promotionUuid 促销活动uuid
     * @return 返回选择的属性DTO
     */
 //   public SpecChooseDTO selectSpec(String productUuid, String selectUuidList, String promotionUuid);

    /**
     * 提供查询商品列表及筛选条件
     * @param queryParam
     * @return
     */
    public Map<String, Object> searchProductAndScreenAttr(Map<String,String> queryParam);

    /**
     * 添加商品收藏
     * @param sku 商品sku
     * @return 返回结果：收藏状态：1：收藏成功。2：收藏失败 3：已经收藏 ；收藏记录的uuid
     */
    public String addProductFavorite(String sku);

    /**
     * 获取商品收藏列表
     * @return 返回收藏列表
     */
    public int getProductFavorite();


    /**
     * 获取商品描述信息
     * @param sku
     * @return
     */
    ProductDescDTO getProductDetail(String sku);

    /**
     * 根据商品uuid查询各种评价的数量
     *
     * @param productUuid 商品uuid
     * @return 商品评价DTO
     */
    ProductAppraiseScoreDTO getAppraiseCount(String productUuid);

    /**
     * 获取商品评价信息接口
     *
     * @param productUuid 商品uuid
     * @param type 类型
     * @param nowPage 当前页面
     * @param pageShow 显示条数
     * @return 返回评价信息
     */

    public Pagination getCommentsNew(String productUuid, String type, int nowPage, int pageShow);

    /**
     * 根据商品uuid查询商品详细信息
     *
     * @param productUuid 商品uuid
     * @return 商品详细信息DTO
     */
    CompProductDetailDTO getProductDetailInfo(String productUuid);

    /**
     * 通过商品sku获取店铺uuid
     * @param sku
     * @return
     */

    String getStoreUuidByProductUuid(String sku);
    /**
     * 根据商户查询商户某一分类对应的同级分类
     */
    List<ProductCategoryDTO> getSameLevelProductCategorysByStoreUuid(String storeUuid);

    /**
     * 选择属性返回商品信息接口
     * @param selectUuidList 选择的属性列表
     * @return 返回选择的属性DTO
     */
    SpecChooseDTO selectSpec(String sku, String selectUuidList);
}
