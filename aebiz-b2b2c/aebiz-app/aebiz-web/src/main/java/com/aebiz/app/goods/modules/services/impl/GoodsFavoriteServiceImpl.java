package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.app.goods.modules.models.Goods_favorite;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.services.GoodsFavoriteService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsFavoriteServiceImpl extends BaseServiceImpl<Goods_favorite> implements GoodsFavoriteService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 保存商品收藏信息(调用方法前要先验证accountId、goods_product对应的记录是否存在)
     * @param accountId 会员id
     * @param goods_product 货品
     * @return
     */
    @Override
    public Goods_favorite saveData(String accountId, Goods_product goods_product) {
        Goods_main goods_main = goods_product.getGoodsMain();
        Goods_favorite goods_favorite = new Goods_favorite();
        goods_favorite.setAccountId(accountId);
        goods_favorite.setGoodsId(goods_product.getGoodsId());
        goods_favorite.setGoodsName(goods_main.getName());
        goods_favorite.setGoodsVersion(goods_main.getGoodsVersion());
        goods_favorite.setProductId(goods_product.getId());
        goods_favorite.setProductName(goods_product.getName());
        goods_favorite.setSku(goods_product.getSku());
        goods_favorite.setProductVersion(goods_product.getProductVersion());
        goods_favorite.setFavoritePrice(goods_product.getSalePrice());
        goods_favorite.setFavoriteTime((int)(System.currentTimeMillis() / 1000));
        return dao().insert(goods_favorite);
    }

    @Override
    @Transactional
    public Map<String, Object> selectDataAll(String accountId, Integer page, Integer rows, Integer status) {
        String sq="SELECT gf.*,gi.imgAlbum,gp.salePrice,gp.spec FROM goods_favorite gf\n" +
                "LEFT JOIN goods_image gi ON gf.goodsId=gi.goodsId\n" +
                "LEFT JOIN goods_product gp ON gf.sku=gp.sku\n" +
                "WHERE gf.accountId='"+accountId+"' and gf.delFlag=0  and gi.defaultValue=1";
        Sql sql1 = Sqls.queryRecord(sq+" limit "+(page-1)*rows+","+rows);//
        dao().execute(sql1);
        int totle = dao().count("goods_favorite gf \n" +
                "LEFT JOIN goods_image gi ON gf.goodsId=gi.goodsId\n" +
                "LEFT JOIN goods_product gp ON gf.sku=gp.sku\n" +
                "WHERE gf.accountId='"+accountId+"' and gf.delFlag=0  and gi.defaultValue=1");
        List<Goods_favorite> list = sql1.getList(Goods_favorite.class);
        Map<String,Object> map = new HashMap<String,Object>();
        int total = (totle+rows-1)/rows;
        map.put("totalPage", total);
        map.put("page", page);
        map.put("status", status);
        map.put("records", totle);
        map.put("count", list.size());
        map.put("rowList", list);
        return map;
    }

    @Override
    @Transactional
    public Map<String, Object> selectData(String content,String accountId, Integer page, Integer rows, Integer status) {
        String sq="SELECT gf.*,gi.imgAlbum,gp.salePrice,gp.spec FROM goods_favorite gf\n" +
                "LEFT JOIN goods_image gi ON gf.goodsId=gi.goodsId\n" +
                "LEFT JOIN goods_product gp ON gf.sku=gp.sku\n" +
                "WHERE gf.accountId='"+accountId+"' and gf.goodsName like '%"+content+"%'and gf.delFlag=0 and gi.defaultValue=1";

        Sql sql1 = Sqls.queryRecord(sq+" limit "+(page-1)*rows+","+rows);//
        dao().execute(sql1);
        int totle = dao().count("goods_favorite gf \n" +
                "LEFT JOIN goods_image gi ON gf.goodsId=gi.goodsId\n" +
                "LEFT JOIN goods_product gp ON gf.sku=gp.sku\n" +
                "WHERE gf.accountId='"+accountId+"' and gf.goodsName like '%"+content+"%'and gf.delFlag=0  and gi.defaultValue=1");

        List<Goods_favorite> list = sql1.getList(Goods_favorite.class);

        Map<String,Object> map = new HashMap<String,Object>();
        int total = (totle+rows-1)/rows;
        map.put("totalPage", total);
        map.put("page", page);
        map.put("status", status);
        map.put("records", totle);
        map.put("count", list.size());
        map.put("rowList", list);
        return map;
    }
}
