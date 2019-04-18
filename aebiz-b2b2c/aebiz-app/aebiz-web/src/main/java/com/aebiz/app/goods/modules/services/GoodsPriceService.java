package com.aebiz.app.goods.modules.services;

import com.aebiz.app.goods.modules.models.Goods_price_level;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.goods.modules.models.Goods_price;

import java.util.List;
import java.util.Map;

public interface GoodsPriceService extends BaseService<Goods_price>{

    /**
     * 保存货品价格
     *
     * @param priceList 价格列表
     */
    void save(List<Goods_price> priceList);

    /**
     * 批量更新
     * @param priceList
     */
    void updates(List<Goods_price> priceList);

    void updatePriceOnStrategy(List<Goods_price> priceList, String productId, String storeId);

    /**
     * 保存等级价格
     * @param list
     */
    void saveLevelPrice(List<Goods_price_level> list);

    void vDelete(String id, String pricelevelId, String storeId);

    /**
     *  取价
     * @param sku 货号
     * @param client 销售终端
     * @param countyCode 销售区县Code
     * @param memberLevelId 会员等级ID
     * @return
     */
    int price(String sku, String countyCode, int client, String memberLevelId);

    /**
     * 取销售价
     * @param sku 货号
     * @param areaCode 片区code
     * @param provinceCode 省code
     * @param cityCode 市code
     * @param saleToClient 销售终端
     * @param saleToMember 销售对象
     * @param saleToMemberLevel 对象等级
     * @return 销售价
     */
    int getSalePrice(String sku, String areaCode, String provinceCode, String cityCode, int saleToClient, int saleToMember, String saleToMemberLevel);

}
