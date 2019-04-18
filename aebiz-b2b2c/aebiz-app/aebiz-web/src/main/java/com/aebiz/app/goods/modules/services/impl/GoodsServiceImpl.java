package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.app.goods.modules.models.*;
import com.aebiz.app.goods.modules.models.em.GoodsCheckStatusEnum;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.models.em.GoodsSaleTimeByEnum;
import com.aebiz.app.goods.modules.models.em.GoodsStatusEnum;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.shop.modules.models.Shop_estemp;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.shop.modules.services.ShopConfigService;
import com.aebiz.app.shop.modules.services.ShopEstempService;
import com.aebiz.app.store.modules.models.Store_goodsclass;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.rabbit.RabbitMessage;
import com.aebiz.baseframework.rabbit.RabbitProducer;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.Daos;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("unchecked")
public class GoodsServiceImpl extends BaseServiceImpl<Goods_main> implements GoodsService {

    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private GoodsProductAreaServiceImpl goodsProductAreaService;
    @Autowired
    private GoodsProductService goodsProductService;
    @Autowired
    private GoodsPriceService goodsPriceService;
    @Autowired
    private GoodsImageService goodsImageService;
    @Autowired
    private GoodsTagService goodsTagService;
    @Autowired
    private GoodsMainChecklogService goodsMainChecklogService;
    @Autowired
    private ShopConfigService shopConfigService;
    @Autowired
    private ShopAreaService shopAreaService;
    @Autowired
    private ShopEstempService shopEstempService;
    @Autowired
    private RabbitProducer rabbitProducer;

    public NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, String linkName, Goods_main goods, NutMap fieldMap){

        StringBuilder sb = new StringBuilder("select main.* from goods_main main where ");
        NutMap valuesMap = NutMap.NEW();

        sb.append("main.delFlag = @delFlag ");
        valuesMap.put("delFlag", goods.getDelFlag());

        if (goods.getStatus() != null) {
            sb.append("and main.status = @status ");
            valuesMap.put("status", goods.getStatus());
        }
        if (goods.getCheckStatus() != null) {
            sb.append("and main.checkStatus = @checkStatus ");
            valuesMap.put("checkStatus", goods.getCheckStatus());
        }
        if (Strings.isNotBlank(goods.getName())) {
            sb.append("and main.name like @name ");
            valuesMap.put("name", Sqls.escapeSqlFieldValue("%" + goods.getName() + "%"));
        }
        if (Strings.isNotBlank(goods.getClassId())) {
            sb.append("and main.classId = @classId ");
            valuesMap.put("classId", goods.getClassId());
        }
        if (Strings.isNotBlank(goods.getTypeId())) {
            sb.append("and main.typeId = @typeId ");
            valuesMap.put("typeId", goods.getTypeId());
        }
        if (Strings.isNotBlank(goods.getBrandId())) {
            sb.append("and main.brandId = @brandId ");
            valuesMap.put("brandId", goods.getBrandId());
        }
        if (fieldMap.containsKey("hasSpec")) {
            sb.append("and main.hasSpec = @hasSpec ");
            valuesMap.put("hasSpec", fieldMap.getInt("hasSpec"));
        }
        if (fieldMap.containsKey("tagId")) {
            sb.append("and exists(select 1 from goods_main_tag tagList where tagList.goodsId = main.id and tagList.tagId = @tagId) ");
            valuesMap.put("tagId", fieldMap.getString("tagId"));
        }
        if (fieldMap.containsKey("storeId")) {
            sb.append("and main.storeId = @storeId ");
            valuesMap.put("storeId", fieldMap.getString("storeId"));
        }

        Sql countSql = Sqls.create(sb.toString());
        countSql.params().putAll(valuesMap);
        NutMap re = new NutMap();
        sb.append(" ORDER BY ");
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                sb.append(" main.").append(Sqls.escapeSqlFieldValue(col.getData()).toString()).append(" ").append(order.getDir()).append(", ");
            }
        }
        sb.append(" main.opAt desc ");

        Sql orderSql = Sqls.create(sb.toString());
        orderSql.params().putAll(valuesMap);
        Pager pager = new OffsetPager(start, length);
        pager.setRecordCount((int) Daos.queryCount(this.dao(), countSql));
        orderSql.setPager(pager);
        orderSql.setCallback(Sqls.callback.entities());
        orderSql.setEntity(dao().getEntity(Goods_main.class));
        this.dao().execute(orderSql);
        List<Goods_main> list = orderSql.getList(Goods_main.class);
        this.dao().fetchLinks(list, linkName);
        re.put("recordsFiltered", pager.getRecordCount());
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;

    }

    @Override
    @Transactional
    public void add(Goods_main goods) {
        //保存商品
        dao().insert(goods);

        //保存商品与前台分类的关联关系
        dao().insertRelation(goods,"storeGoodsClassList");
        //保存商品与标签的关联关系
        dao().insertRelation(goods,"tagList");
        //保存商品相册图
        dao().insertLinks(goods, "imageList");
        //保存货品
        saveProduct(goods.getMallId(), goods.getStoreId(), goods.getId(), goods.getProductList());

        //添加审核信息
        addCheck(goods);

        //mq商品同步es
        if (SpringUtil.isRabbitEnabled()) {
            String exchange = "topicExchange";
            String routeKey = "topic.es.goods";
            RabbitMessage msg = new RabbitMessage(exchange, routeKey, NutMap.NEW().addv("goodsId",goods.getId()).addv("action","create"));
            rabbitProducer.sendMessage(msg);
        }

       /* //加入搜索引擎job
        Shop_estemp estemp = new Shop_estemp();
        estemp.setAction("create");
        estemp.setGoodsId(goods.getId());
        shopEstempService.insert(estemp);*/
    }

    @Override
    @Transactional
    public void save(Goods_main goods) {

        //版本号每修改一次+1
        this.update(Chain.makeSpecial("goodsVersion", "+1"), Cnd.where("id","=", goods.getId()));

        //更新价格
        synClearPrice(goods, this.fetch(goods.getId()), goods.getProductList());

        //更新商品
        this.updateIgnoreNull(goods);

        //更新商品与前台分类的关联关系
        dao().clearLinks(goods,"storeGoodsClassList");
        dao().insertRelation(goods,"storeGoodsClassList");
        //更新商品与标签的关联关系
        dao().clearLinks(goods, "tagList");
        dao().insertRelation(goods,"tagList");
        //更新商品相册图
        dao().clearLinks(goods, "imageList");
        dao().insertLinks(goods, "imageList");
        //保存货品
        saveProduct(goods.getMallId(), goods.getStoreId(), goods.getId(), goods.getProductList());

        //添加审核信息
        addCheck(goods);

        //mq商品同步es
        if (SpringUtil.isRabbitEnabled()) {
            String exchange = "topicExchange";
            String routeKey = "topic.es.goods";
            RabbitMessage msg = new RabbitMessage(exchange, routeKey, NutMap.NEW().addv("goodsId",goods.getId()).addv("action","update"));
            rabbitProducer.sendMessage(msg);
        }

       /* //加入搜索引擎job
        Shop_estemp estemp = new Shop_estemp();
        estemp.setAction("update");
        estemp.setGoodsId(goods.getId());
        shopEstempService.insert(estemp);*/
    }

    @Override
    @Transactional
    public void vDeleteGoods(String id, String storeId) {
        this.update(Chain.make("delFlag", true), Cnd.where("id","=", id).and("storeId", "=", storeId));

        //加入搜索引擎
        Shop_estemp estemp = new Shop_estemp();
        estemp.setAction("delete");
        estemp.setGoodsId(id);
        shopEstempService.insert(estemp);
    }

    @Override
    @Transactional
    public void vDeleteGoods(String[] ids, String storeId) {
        this.update(Chain.make("delFlag", true), Cnd.where("id","in", ids).and("storeId", "=", storeId));

        //加入搜索引擎
        for (String id : ids) {
            Shop_estemp estemp = new Shop_estemp();
            estemp.setAction("delete");
            estemp.setGoodsId(id);
            shopEstempService.insert(estemp);
        }
    }

    @Override
    @Transactional
    public void updown(String id, boolean sale, String storeId) {

        if (sale) {//上架
            this.update(Chain.make("sale", true).add("status", GoodsStatusEnum.SALE.getKey()), Cnd.where("id", "=", id).and("storeId", "=", storeId));
        } else {
            this.update(Chain.make("sale", false).add("status", GoodsStatusEnum.OFFSALE.getKey()), Cnd.where("id", "=", id).and("storeId", "=", storeId));
        }

        //mq商品同步es
        if (SpringUtil.isRabbitEnabled()) {
            String exchange = "topicExchange";
            String routeKey = "topic.es.goods";
            RabbitMessage msg = new RabbitMessage(exchange, routeKey, NutMap.NEW().addv("goodsId",id).addv("action","update"));
            rabbitProducer.sendMessage(msg);
        }
       /* //加入搜索引擎
        Shop_estemp estemp = new Shop_estemp();
        estemp.setAction("update");
        estemp.setGoodsId(id);
        shopEstempService.insert(estemp);*/
    }

    @Override
    @Transactional
    public void updowns(String[] ids, boolean sale, String storeId) {
        for (String id : ids) {
            updown(id, sale, storeId);
        }
    }

    @Override
    @Transactional
    public void saveTagLink(String[] ids, String[] tagIds) {
        List<Goods_tag> tagList = goodsTagService.query(Cnd.where("id", "in", tagIds));
        List<Goods_main> goodsList = this.query(Cnd.where("id", "in", ids));
        dao().clearLinks(goodsList,"tagList");
        for (Goods_main goods : goodsList) {
            goods.setTagList(tagList);
            dao().insertRelation(goods,"tagList");

            //mq商品同步es
            if (SpringUtil.isRabbitEnabled()) {
                String exchange = "topicExchange";
                String routeKey = "topic.es.goods";
                RabbitMessage msg = new RabbitMessage(exchange, routeKey, NutMap.NEW().addv("goodsId",goods.getId()).addv("action","update"));
                rabbitProducer.sendMessage(msg);
            }
           /* //加入搜索引擎
            Shop_estemp estemp = new Shop_estemp();
            estemp.setAction("update");
            estemp.setGoodsId(goods.getId());
            shopEstempService.insert(estemp);*/
        }

    }

    @Override
    @Transactional
    public void markBreakrule(String[] ids, String comment) {
        check(ids, GoodsCheckStatusEnum.ILLEGL.getKey(), comment);
    }

    @Override
    @Transactional
    public void check(String goodsId, Integer checkStatus, String comment) {
        Goods_main goods = this.fetch(goodsId);
        Chain chain = Chain.make("checkStatus", checkStatus);
        if (GoodsCheckStatusEnum.PASS.getKey() == checkStatus) {
            if (goods.getSaleTimeBy() == GoodsSaleTimeByEnum.NOW.getKey()) {
                chain.add("sale", true);
                chain.add("status", GoodsStatusEnum.SALE.getKey());
            } else {
                chain.add("sale", false);
                chain.add("status", GoodsStatusEnum.WAITSALE.getKey());
            }
            this.update(chain, Cnd.where("id", "=", goodsId));
        } else if (GoodsCheckStatusEnum.NOPASS.getKey() == checkStatus) {
            chain.add("sale", false);
            chain.add("status", GoodsStatusEnum.NOPASS.getKey());
            this.update(chain, Cnd.where("id", "=", goodsId));

        } else if (GoodsCheckStatusEnum.ILLEGL.getKey() == checkStatus) {
            chain.add("sale", false);
            chain.add("status", GoodsStatusEnum.OFFSALE.getKey());
            this.update(chain, Cnd.where("id", "=", goodsId));
        }
        goodsMainChecklogService.addCheckLog(goodsId, checkStatus, comment, StringUtil.getUid());


        //mq商品同步es
        if (SpringUtil.isRabbitEnabled()) {
            String exchange = "topicExchange";
            String routeKey = "topic.es.goods";
            RabbitMessage msg = new RabbitMessage(exchange, routeKey, NutMap.NEW().addv("goodsId",goodsId).addv("action","update"));
            rabbitProducer.sendMessage(msg);
        }
       /* //加入搜索引擎
        Shop_estemp estemp = new Shop_estemp();
        estemp.setAction("update");
        estemp.setGoodsId(goodsId);
        shopEstempService.insert(estemp);*/
    }

    @Override
    @Transactional
    public void check(String[] goodsIds, Integer checkStatus, String comment) {
        for (String goodsId : goodsIds) {
            check(goodsId, checkStatus, comment);
        }
    }

    @Override
    public Map getStatisticMap(String storeId) {
        StringBuilder sb = new StringBuilder("select CONCAT('check',a.checkStatus), count(*) from goods_main a where a.delFlag = '0' group by a.checkStatus ");
        if(!Strings.isEmpty(storeId)){
            sb.append("and a.storeId ="+storeId);
        }
        sb.append(" UNION ");
        sb.append("SELECT CONCAT('status',a.status), COUNT(*) FROM goods_main a WHERE a.delFlag = '0'");
        if(!Strings.isEmpty(storeId)){
            sb.append("and a.storeId ="+storeId);
        }
        sb.append(" GROUP BY a.status ");
        return this.getMap(Sqls.create(sb.toString()));
    }

    @Override
    public boolean isDuplicatedSku(Goods_main goods) {
        if (!Lang.isEmpty(goods.getProductList())) {
            List<Goods_product> productList = goods.getProductList().stream()
                    .filter(x -> Strings.isNotBlank(x.getSku()))
                    .collect(Collectors.toList());
            if (!Lang.isEmpty(productList)) {
                //前端判重
                Object[] objects = productList.stream().map(x -> x.getSku()).distinct().toArray();
                if (productList.size() != objects.length) {
                    return true;
                }
                //数据库判重
                List<Goods_product> exists = productList.stream().filter(p -> {
                    Cnd cnd = Cnd.where("sku", "=", p.getSku());
                    //不和自己比较
                    if (Strings.isNotBlank(p.getId())) {
                        cnd.andNot("id", "=", p.getId());
                    }
                    return !Lang.isEmpty(goodsProductService.query(cnd));
                }).collect(Collectors.toList());

                //可以继续处理exists，现已满足，不做处理
                return !Lang.isEmpty(exists);
            }
        }
        return false;
    }

    /**
     * 保存商品前台分类
     * @param goodsId 商品ID
     * @param storeGoodsclassIds 前台分类IDS
     */
    private void saveStoreGoodsclassLink(String goodsId, String[] storeGoodsclassIds) {
        if (!Lang.isEmpty(storeGoodsclassIds)) {
            Goods_main goods = this.fetch(goodsId);
            List<Store_goodsclass> storeGoodsclassList = new ArrayList<>(storeGoodsclassIds.length);
            for (int i = 0; i < storeGoodsclassIds.length; i++) {
                Store_goodsclass goodsclass = new Store_goodsclass();
                goodsclass.setId(storeGoodsclassIds[i]);
                storeGoodsclassList.add(goodsclass);
            }
            goods.setStoreGoodsClassList(storeGoodsclassList);
            this.dao().insertRelation(goods,"storeGoodsClassList");
        }
    }

    /**
     * 保存货品
     *
     * @param mallId 商场ID（预留）
     * @param storeId 商城ID
     * @param goodsId 商品ID
     * @param products 货品
     */
    private void saveProduct(String mallId, String storeId, String goodsId, List<Goods_product> products) {
        List<String> ids = new LinkedList<>();
        for (Goods_product product : products) {
            for (Goods_product_area area : product.getAreaList()) {
                area.setStoreId(storeId);
            }
            if (Strings.isBlank(product.getId())) {
                product.setId(null);
                product.setMallId(mallId);
                product.setStoreId(storeId);
                product.setGoodsId(goodsId);
                //使用默认SKU
                if (Strings.isBlank(product.getSku())) {
                    product.setSku(generateSKU());
                }
                goodsProductService.dao().insertWith(product, "areaList");
                goodsProductService.dao().insertRelation(product, "memberTypeList");
            } else {
                //版本号每修改一次+1
                goodsProductService.update(Chain.makeSpecial("productVersion", "+1"), Cnd.where("id","=", product.getId()));
                //使用默认SKU
                if (Strings.isBlank(product.getSku())) {
                    product.setSku(generateSKU());
                }
                goodsProductService.updateIgnoreNull(product);
                goodsProductService.dao().clearLinks(product, "areaList");
                goodsProductService.dao().insertLinks(product, "areaList");
                goodsProductService.dao().clearLinks(product, "memberTypeList");
                goodsProductService.dao().insertRelation(product, "memberTypeList");
            }
            ids.add(product.getId());
        }

        //删除无效的货品及关联信息
        Cnd cnd = Cnd.where("goodsId", "=", goodsId);
        if (ids.size() > 0) {
            cnd.and("id","not in",ids);
        }
        List<Goods_product> delProductList = goodsProductService.query(cnd);
        if (!Lang.isEmpty(delProductList)) {
            goodsProductService.dao().clearLinks(delProductList, "^(memberTypeList|areaList)$");
            goodsProductService.dao().delete(delProductList);
        }
    }

    /**
     * 添加审核信息
     *
     */
    private void addCheck(Goods_main goods) {
        if (goods.getStatus() == GoodsStatusEnum.SALE.getKey()) {
            this.update(Chain.make("sale", true).add("checkStatus", GoodsCheckStatusEnum.PASS.getKey()).add("status", GoodsStatusEnum.SALE.getKey()), Cnd.where("id", "=", goods.getId()));
        } else if (goods.getStatus() == GoodsStatusEnum.WAITSALE.getKey()) {
            this.update(Chain.make("sale", false).add("checkStatus", GoodsCheckStatusEnum.PASS.getKey()).add("status", GoodsStatusEnum.WAITSALE.getKey()), Cnd.where("id", "=", goods.getId()));
        } else if (goods.getStatus() == GoodsStatusEnum.CHECKING.getKey()) {
            this.update(Chain.make("sale", false).add("checkStatus", GoodsCheckStatusEnum.WAIT.getKey()).add("status", GoodsStatusEnum.CHECKING.getKey()), Cnd.where("id", "=", goods.getId()));
            goodsMainChecklogService.addApplyLog(goods.getId(), goods.getStoreId(), StringUtil.getStoreUid());
        }
    }

    /**
     * 清除相关的价格信息
     *
     * @param newGoods 为持久化goods
     * @param oldGoods 已持久化的goods
     * TODO 计算价格
     */
    private void synClearPrice(Goods_main newGoods, Goods_main oldGoods, List<Goods_product> products) {
        if (Lang.isEmpty(newGoods) || Lang.isEmpty(oldGoods)) {
            return ;
        }
        //销售区域
        /*if (newGoods.isSaleToAllAera() ^ oldGoods.isSaleToAllAera()) {
            goodsPriceService.dao().deleteWith(goodsPriceService.query(Cnd.where("goodsId", "=", oldGoods.getId()).and("saleToAllAera", "=", oldGoods.isSaleToAllAera())), "priceLevelList");
        } else if (!newGoods.isSaleToAllAera()){//非全部销售区域
            goodsPriceService.dao().deleteWith(goodsPriceService.query(Cnd.where("goodsId", "=", oldGoods.getId()).and("saleCity", "not in", shopAreaCodes)), "priceLevelList");
        }*/


        Goods_price price = null;
        //PC终端销售有到无
        if (oldGoods.isSaleClientPC() && !newGoods.isSaleClientPC()) {
            price = goodsPriceService.fetch(Cnd.where("goodsId", "=", oldGoods.getId()).and("saleClient", "=", GoodsSaleClientEnum.PC.getKey()));
        }
        //WAP终端销售有到无
        if (oldGoods.isSaleClientWAP() && !newGoods.isSaleClientWAP()) {
            price = goodsPriceService.fetch(Cnd.where("goodsId", "=", oldGoods.getId()).and("saleClient", "=", GoodsSaleClientEnum.WAP.getKey()));

        }
        //TV终端销售有到无
        if (oldGoods.isSaleClientTV() && !newGoods.isSaleClientTV()) {
            price = goodsPriceService.fetch(Cnd.where("goodsId", "=", oldGoods.getId()).and("saleClient", "=", GoodsSaleClientEnum.TV.getKey()));
        }
        if (price != null) {
            goodsPriceService.dao().deleteWith(price, "priceLevelList");
        }


        //销售对象类型变更
        if (!Lang.isEmpty(products)) {
            List<String> productIds = new ArrayList<>();
            for (Goods_product product : products) {
                if (Strings.isNotBlank(product.getId())){

                }
            }
        }
    }

    /**
     * 生成默认SKU
     */
    private String generateSKU() {
        return StringUtil.getRndNumber(20);
    }

}
