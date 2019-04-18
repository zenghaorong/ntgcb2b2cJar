package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.app.goods.modules.models.Goods_price_level;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.services.GoodsProductAreaService;
import com.aebiz.app.goods.modules.services.GoodsPriceLevelService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.goods.modules.models.Goods_price;
import com.aebiz.app.goods.modules.services.GoodsPriceService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsPriceServiceImpl extends BaseServiceImpl<Goods_price> implements GoodsPriceService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private GoodsPriceLevelService goodsPriceLevelService;
    @Autowired
    private GoodsProductService goodsProductService;
    @Autowired
    private GoodsProductAreaService goodsProductAreaService;

    @Transactional
    public void save(List<Goods_price> priceList) {
        for (Goods_price price : priceList) {

            Cnd cnd = Cnd.where("sku", "=", Strings.sNull(price.getSku()));

            //销售区域
            if (price.isSaleToAllAera()) {
                cnd.and("saleToAllAera", "=", price.isSaleToAllAera());
            }
            if (Strings.isNotBlank(price.getSaleArea())) {
                cnd.and("saleArea", "=", price.getSaleArea());
            }
            if (Strings.isNotBlank(price.getSaleProvince())) {
                cnd.and("saleProvince", "=", price.getSaleProvince());
            }
            if (Strings.isNotBlank(price.getSaleCity())) {
                cnd.and("saleCity", "=", price.getSaleCity());
            }

            //销售终端
            cnd.and("saleClient", "=", price.getSaleClient());

            //销售对象
            cnd.and("saleToMemberType", "=", price.getSaleToMemberType());

            dao().deleteWith(this.query(cnd, "priceLevelList"), "priceLevelList");

            //保存
            dao().insertWith(price, "priceLevelList");
        }
    }

    @Transactional
    public void updates(List<Goods_price> priceList) {
        for (Goods_price price : priceList) {
            this.update(Chain.make("salePrice", price.getSalePrice()), Cnd.where("id", "=", price.getId()));
        }
    }

    @Transactional
    public void updatePriceOnStrategy(List<Goods_price> priceList, String productId, String storeId) {
        Goods_product product = goodsProductService.fetchLinks(goodsProductService.fetch(productId), "goodsMain");
        for (Goods_price price : priceList) {
            price.setStoreId(storeId);
            price.setGoodsId(product.getGoodsMain().getId());
        }
        save(priceList);
    }

    @Transactional
    public void saveLevelPrice(List<Goods_price_level> list) {
        for (Goods_price_level lvp : list) {
            goodsPriceLevelService.updateIgnoreNull(lvp);
        }
    }

    @Transactional
    public void vDelete(String priceId, String priceLevelId, String storeId) {
        if (Strings.isNotBlank(priceLevelId)) {
            goodsPriceLevelService.vDelete(priceLevelId);
        }
        this.vDelete(priceId);
    }

    public int price(String sku, String countyCode, int client, String memberLevelId) {
        Cnd cnd = Cnd.where("sku", "=", Strings.sNull(sku));
        //TODO 根据销售区域终端会员类型等级取价
        Goods_product product = goodsProductService.fetch(cnd);
        return product.getSalePrice();
    }

    public int getSalePrice(String sku, String areaCode, String provinceCode, String cityCode, int saleToClient, int saleToMember, String saleToMemberLevel) {
        Cnd cnd = Cnd.where("sku", "=", Strings.sNull(sku));

        //销售区域
        if (Strings.isNotBlank(areaCode)) {
            cnd.and("saleArea", "=", areaCode);
        }
        if (Strings.isNotBlank(provinceCode)) {
            cnd.and("saleProvince", "=", provinceCode);
        }
        if (Strings.isNotBlank(cityCode)) {
            cnd.and("saleCity", "=", cityCode);
        }

        //销售终端
        cnd.and("saleClient", "=", saleToClient);

        //销售对象
        cnd.and("saleToMemberType", "=", saleToMember);

        Goods_product product = goodsProductService.fetch(Cnd.where("sku", "=", sku));
        int salePrice = product.getSalePrice();
        Goods_price price = this.fetch(cnd);
        this.fetchLinks(price, "priceLevelList", Cnd.where("memberLevelId", "=", Strings.sNull(saleToMemberLevel)));
        if (Lang.isEmpty(price)) {                              //1.不在价格表中，则取销售价
            return salePrice;
        } else if (!Lang.isEmpty(price.getPriceLevelList())){   //2.价格表中存在且等级价也存在，则取等级价
            salePrice = price.getPriceLevelList().get(0).getMemberLevelPrice();
        } else {                                                //3.无等级价，则取价格表中的销售价
            salePrice = price.getSalePrice();
        }
        return salePrice;
    }

}
