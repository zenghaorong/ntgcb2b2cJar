package com.aebiz.app.goods.modules.services;

import com.aebiz.app.goods.commons.vo.GoodsProductVO;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.lang.util.NutMap;

import java.util.List;

public interface GoodsProductService extends BaseService<Goods_product>{

    /**
     * 生成SKU前缀
     * @return SKU前缀
     */
    String getSkuPrefix();

    /**
     * 更新货品库存
     * @param sku
     * @param buyNum
     * @param opType opType为 true,则根据方式进行减库存,为false 看是下单时候减库存方式则加库存，不是则库存不变
     */
    void updateStock(String sku,Integer buyNum,boolean opType);

    /**
     * 更新货品价格
     * @param goodsProducts
     */
    void updatePrice(List<Goods_product> goodsProducts);

    NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, NutMap fieldMap);

    NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Goods_main goods, Goods_product product, String storeId);

    //根据关键词模糊查询商品名称
    public List<String> getByLikeName(String keyWord);

    /**
     * 分页查询货品
     * @param length
     * @param start
     * @param draw
     * @param orders
     * @param columns
     * @param goods 目前只有查询商品分类和商品名称
     * @param storeId
     * @param linkName
     * @return
     */
    NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Goods_main goods,String storeId,String linkName);

    /**
     * 通过货品id数组取货品列表
     * @param productUuid
     * @return
     */
    List<Goods_product> getMutiProductMainModel(String[] productUuid);

    /**
     * 根据sku取获取货品图
     * @param sku
     * @return 存在，返回图片路径；不存在，返回null
     *
     */
    String getProductImage(String sku);

    /**
     * 根据sku及销售终端取获取货品图
     * @param sku
     * @param client
     * @return 存在，返回图片路径；不存在，返回null
     *
     */
    String getProductImage(String sku, GoodsSaleClientEnum client);

    /**
     * 根据sku取货品，包含商品及商家信息
     * @param sku
     * @return
     */
    Goods_product getBySku(String sku);

    /**
     * 商品收藏接口
     * @param sku
     * @return
     */
   String  saveProductFavorite(String customerUuid, String sku);

    /**
     * 验证货品是否有库存
     * @param products
     * @return
     */
   boolean validateStock(List<GoodsProductVO> products);

    /**
     * 根据接收的规格属性去匹配sku
     */

    String getSkuBySpecs(String sku,String[] specArr);
}
