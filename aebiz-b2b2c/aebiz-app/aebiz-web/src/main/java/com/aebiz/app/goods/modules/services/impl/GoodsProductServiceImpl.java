package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.app.goods.commons.vo.GoodsProductVO;
import com.aebiz.app.goods.modules.models.*;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.models.em.GoodsStockOffTypeEnum;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.Daos;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class GoodsProductServiceImpl extends BaseServiceImpl<Goods_product> implements GoodsProductService {


    @Autowired
    private GoodsClassService goodsClassService;
    @Autowired
    private GoodsImageService goodsImageService;

    @Autowired
    private GoodsFavoriteService goodsFavoriteService;


    @Override
    public NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, NutMap fieldMap) {
        StringBuilder sb = new StringBuilder("select ");
        sb.append("product.id, product.name, product.sku, product.costPrice, product.salePrice, product.marketPrice, ");
        sb.append("product.weight, product.length, product.width, product.height, product.stock, product.buyMin, product.buyMax, ");
        sb.append("product.defaultValue, product.productVersion, product.orderVerify, product.enabled, ");
        sb.append("product.partitionBy, product.saleToAllAera, product.saleToAllMemberType, product.saleToMemberType, ");
        sb.append("product.deliveryTime, product.serializable, product.location, product.opBy, product.opAt, ");
        sb.append("goods.id as goodsId, goods.name as goodsName  ");
        sb.append("from goods_product product ");
        sb.append("left join goods_main goods on goods.id = product.goodsId ");
        sb.append("where 1=1 ");

        NutMap valuesMap = NutMap.NEW();
        if (fieldMap.containsKey("delFlag")) {
            sb.append("and product.delFlag = @delFlag ");
            valuesMap.put("delFlag", fieldMap.getBoolean("delFlag"));
        }
        if (fieldMap.containsKey("storeId")) {
            sb.append("and product.storeId = @storeId ");
            valuesMap.put("storeId", fieldMap.getString("storeId"));
        }
        if (fieldMap.containsKey("sale")) {
            sb.append("and goods.sale = @sale ");
            valuesMap.put("sale", fieldMap.getBoolean("sale"));
        }
        if (fieldMap.containsKey("status")) {
            sb.append("and goods.status = @status ");
            valuesMap.put("status", fieldMap.getInt("status"));
        }
        if (fieldMap.containsKey("enabled")) {
            sb.append("and product.enabled = @enabled ");
            valuesMap.put("enabled", fieldMap.getBoolean("enabled"));
        }
        if (fieldMap.containsKey("goodsName")) {
            sb.append("and goods.id = @noticeId ");
            valuesMap.put("noticeId", fieldMap.getString("noticeId"));
        }
        if (fieldMap.containsKey("goodsBrandId")) {
            sb.append("and goods.brandId = @brandId ");
            valuesMap.put("brandId", fieldMap.getString("goodsBrandId"));
        }
        if (fieldMap.containsKey("goodsTypeId")) {
            sb.append("and goods.typeId = @typeId ");
            valuesMap.put("typeId", fieldMap.getString("goodsTypeId"));
        }

        //组装countSql
        Sql countSql = Sqls.create(sb.toString());
        countSql.params().putAll(valuesMap);

        //排序字段
        sb.append(" ORDER BY ");
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                sb.append(Sqls.escapeSqlFieldValue(col.getData()).toString()).append(" ").append(order.getDir()).append(", ");
            }
        }
        sb.append(" product.opAt desc ");//默认排序

        //组装orderSql
        Sql orderSql = Sqls.create(sb.toString());
        orderSql.params().putAll(valuesMap);

        NutMap re = new NutMap();
        Pager pager = new OffsetPager(start, length);
        pager.setRecordCount((int)Daos.queryCount(this.dao(), countSql));
        orderSql.setPager(pager);
        orderSql.setCallback(Sqls.callback.maps());
        this.dao().execute(orderSql);
        re.put("recordsFiltered", Integer.valueOf(pager.getRecordCount()));
        re.put("data", orderSql.getList(Record.class));
        re.put("draw", Integer.valueOf(draw));
        re.put("recordsTotal", Integer.valueOf(length));
        return re;
    }

    @Override
    public NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Goods_main goods, Goods_product product, String storeId) {
        StringBuilder sb = new StringBuilder("select product.* from goods_product product ");
        sb.append("join goods_main goods on goods.id = product.goodsId $condition");
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(goods.getClassId())) {
            cnd.and("goods.classId", "=", goods.getClassId());
        }
        if (Strings.isNotBlank(goods.getName())) {
            SqlExpressionGroup group = new SqlExpressionGroup();
            group.or("goods.name","like", Sqls.escapeSqlFieldValue("%" + goods.getName() + "%")).or("product.sku","like", Sqls.escapeSqlFieldValue("%" + goods.getName() + "%"));
            cnd.and(group);
        }
        cnd.and("goods.storeId", "=", storeId);
        cnd.and("goods.delFlag", "=", false);
        Sql countSql = Sqls.create(sb.toString());
        Sql orderSql = countSql.duplicate();
        countSql.setCondition(cnd);
        Pager pager = new OffsetPager(start, length);
        pager.setRecordCount((int) Daos.queryCount(this.dao(), countSql));

        NutMap re = new NutMap();
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        cnd.desc("goods.opAt");
        cnd.desc("product.opAt");
        orderSql.setCondition(cnd);
        orderSql.setPager(pager);
        orderSql.setCallback(Sqls.callback.entities());
        orderSql.setEntity(dao().getEntity(Goods_product.class));
        this.dao().execute(orderSql);
        List<Goods_product> list = orderSql.getList(Goods_product.class);
        this.dao().fetchLinks(list, "goodsMain");
        re.put("recordsFiltered", pager.getRecordCount());
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }

    /**
     * 分页查询商品：针对订单的手工录单查询商品
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
    public NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Goods_main goods,String storeId,String linkName) {
        StringBuilder sb = new StringBuilder("select product.* from goods_product product ");
        sb.append("join goods_main goods on goods.id = product.goodsId $condition");
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(goods.getClassId())) {
            //查询当前分类
            Goods_class goodsClass =  goodsClassService.fetch(Cnd.NEW().where("delFlag","=",false).and("id","=",goods.getClassId()));
            String conditions = "";
            if(goodsClass == null){
                //当查询的结果为空,则查询不到结果
                conditions = "-1,";
            }else{
               List<Goods_class>  goodsClassList =  goodsClassService.query(Cnd.NEW().where("delFlag","=",false).and("path","like",goodsClass.getPath()+"%"));
                if(goodsClassList != null && goodsClassList.size() > 0){
                    for(Goods_class goods_class:goodsClassList){
                        conditions += (goods_class.getId()+",");
                    }
                }else{
                    //当查询的结果为空,则查询不到结果
                    conditions = "-1,";
                }
            }
            if(conditions.length() >0 ){
                cnd.and("goods.classId","in",conditions.substring(0,conditions.lastIndexOf(",")));
            }

            //cnd.and("goods.classId", "=", goods.getClassId());
        }
        if (Strings.isNotBlank(goods.getName())) {
            SqlExpressionGroup group = new SqlExpressionGroup();
            group.or("goods.name","like", Sqls.escapeSqlFieldValue("%" + goods.getName() + "%")).or("product.sku","like", Sqls.escapeSqlFieldValue("%" + goods.getName() + "%"));
            cnd.and(group);
        }
        cnd.and("goods.storeId", "=", storeId);
        cnd.and("goods.delFlag", "=", false).and("product.enabled","=",true).and("goods.sale","=",true);
        cnd.desc("goods.opAt");
        cnd.desc("product.opAt");
        Sql countSql = Sqls.create(sb.toString());
        Sql orderSql = countSql.duplicate();
        countSql.setCondition(cnd);
        orderSql.setCondition(cnd);
        NutMap re = new NutMap();
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        Pager pager = new OffsetPager(start, length);
        pager.setRecordCount((int) Daos.queryCount(this.dao(), countSql));
        orderSql.setPager(pager);
        orderSql.setCallback(Sqls.callback.entities());
        orderSql.setEntity(dao().getEntity(Goods_product.class));
        this.dao().execute(orderSql);
        List<Goods_product> list = orderSql.getList(Goods_product.class);
        this.dao().fetchLinks(list, linkName);
        if(list != null){
            //组装商品的品牌信息
            for(Goods_product goodsProduct :list){
                Goods_main goodsMain = goodsProduct.getGoodsMain();
                goodsService.fetchLinks(goodsMain,"^(goodsBrand|goodsClass)");
                Goods_class goodsClass = goodsMain.getGoodsClass();
                if(goodsClass != null){
                    LinkedList<String> goodsClassName = new LinkedList<>();
                    goodsClassName.add(goodsClass.getName());
                    while(Strings.isNotBlank(goodsClass.getParentId())){
                        goodsClass  =  goodsClassService.fetch(goodsClass.getParentId());
                        if(goodsClass == null){
                            break;
                        }
                        goodsClassName.add(goodsClass.getName());
                    }
                    Collections.reverse(goodsClassName);
                    goodsMain.getGoodsClass().setName(Strings.join("/",goodsClassName));
                }
            }
        }
        re.put("recordsFiltered", pager.getRecordCount());
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }

    private static final  Log log = Logs.get();

    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private GoodsService goodsService;

    @Override
    public String getSkuPrefix() {

        //TODO 生成sku前缀

        return "sku";
    }

    /**
     * 更新货品库存
     * @param sku
     * @param buyNum
     * @param opType opType为 true,则根据方式进行减库存,为false 看是下单时候减库存方式则加库存，不是则库存不变
     */
    @Override
    @Transactional
    public void updateStock(String sku, Integer buyNum,boolean opType) {
        Cnd cnd = Cnd.NEW();
        cnd.and("sku","=",sku);
        try {
            //sku 是货品的唯一标识
            Goods_product goodsProduct = this.fetch(cnd);
            if(goodsProduct != null){
                Goods_main goodsMain = goodsService.fetch(goodsProduct.getGoodsId());
                if(opType){ //下单或者支付成功
                    //获取减库存方式
                    if(0 ==  goodsMain.getStockOffType()){
                        goodsProduct.setStock(goodsProduct.getStock() - buyNum);
                        goodsProduct.setStockFreeze(goodsProduct.getStockFreeze()+buyNum);
                    }else if(1 ==  goodsMain.getStockOffType()){
                        goodsProduct.setStock(goodsProduct.getStock() - buyNum);
                    }
                }else{ //关闭或者取消订单
                    //获取减库存方式
                    if(GoodsStockOffTypeEnum.ORDER.getKey() ==  goodsMain.getStockOffType()){
                        goodsProduct.setStock(goodsProduct.getStock() + buyNum);
                    }
                }

                this.updateIgnoreNull(goodsProduct);
            }
        }finally {

        }
    }

    @Override
    @Transactional
    public void updatePrice(List<Goods_product> goodsProducts) {
        for (Goods_product product : goodsProducts) {
            this.updateIgnoreNull(product);
        }
    }

    //根据关键词模糊查询商品名称
    public List<String> getByLikeName(String keyWord){
        String str="select o.name from goods_product o ,goods_main  s where s.id=o.goodsId and s.sale=@delFlag" +
                " and o.name like @productName order by o.saleNumAll desc";
        Sql sql=Sqls.create(str).setParam("delFlag",true).setParam("productName","%"+keyWord+"%");
        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                List<String> list = new LinkedList<String>();
                while (rs.next())
                    list.add(rs.getString("name"));
                return list;
            }
        });
        dao().execute(sql);
        List<String> list=sql.getList(String.class);
        return list;
    }
    @Override
    public List<Goods_product> getMutiProductMainModel(String[] productUuid) {
        List<Goods_product> productList=new ArrayList<>();
        if (productUuid != null && productUuid.length > 0) {
            for (int i = 0; i < productUuid.length; i++) {
                Goods_product productModle=this.fetch(productUuid[i]);
                if(productModle !=null){
                    productList.add(productModle);
                }
            }
            return productList;

        }
        return null;
    }

    public String getProductImage(String sku) {
        return getProductImage(sku, GoodsSaleClientEnum.PC);
    }

    public String getProductImage(String sku, GoodsSaleClientEnum client) {
        Goods_product product = this.fetch(Cnd.where("sku", "=", sku));
        if (!Lang.isEmpty(product)) {
            Goods_image image = goodsImageService.fetch(Cnd.where("goodsId", "=", product.getGoodsId()).and("saleClient", "=", client.getKey()).and("defaultValue", "=", true));
            if (!Lang.isEmpty(image)) {
                return image.getImgAlbum();
            }
        }
        return null;
    }

    public Goods_product getBySku(String sku) {
        Goods_product product = this.fetch(Cnd.where("sku", "=", Strings.sNull(sku)));
        if (!Lang.isEmpty(product)) {
            this.fetchLinks(product, "^(goodsMain|storeMain)$");
        }
        return product;
    }


    /**
     * 商品收藏接口
     * 根据会员编号和商品编号保存会员收藏,收藏成功返回1,收藏失败返回2,已经收藏返回3,4货品sku为空或者该商品不存在
     * @param sku
     * @return
     */
    public String  saveProductFavorite(String customerUuid, String sku){
        if(Strings.isBlank(sku)){
            return "4";
        }
      /*  String accountId = StringUtil.getMemberUid();
        if(Strings.isBlank(accountId)){
            return Result.error("当前会员未登陆,请先登陆");
        }*/
        try {

            Goods_favorite favorite = goodsFavoriteService.fetch(Cnd.where("sku","=",sku).and("delFlag","=",false).and("accountId","=",customerUuid));
            if(!Lang.isEmpty(favorite)){
                Goods_product product=this.fetch(Cnd.where("sku","=",sku));
                favorite.setFavoritePrice(product.getSalePrice());
                goodsFavoriteService.update(favorite);
                return "3";
            }
            Goods_product goodsProduct = this.fetch(Cnd.where("sku","=",sku).and("delFlag","=",false));
            if(goodsProduct == null){
                return "4";
            }
            this.fetchLinks(goodsProduct,"goodsMain");
            goodsFavoriteService.saveData(customerUuid,goodsProduct);
           return "1";
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return "2";
        }
    }

    public boolean validateStock(List<GoodsProductVO> products) {
        boolean flag = true;
        for (GoodsProductVO p : products) {
            Goods_product product = this.fetch(Cnd.where("sku", "=", p.getSku()));
            this.fetchLinks(product, "goodsMain");
            if ((!product.getGoodsMain().isSaleZero()) && (p.getNum() > product.getStock())) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 根据接收的规格属性去匹配sku
     */

   public String getSkuBySpecs(String sku,String[] specArr){
        Goods_product product=this.fetch(Cnd.where("sku","=",sku));
       String getSku="";
        if(!Lang.isEmpty(product)){
            //获取上线的启用的商品
           List<Goods_product>  products=this.query(Cnd.where("goodsId","=",product.getGoodsId()).and("sale","=",0).and("enabled","=",1));
           for(Goods_product goodsProduct:products){
               boolean flag=true;
               String spec=goodsProduct.getSpec();
               if(!Strings.isEmpty(spec)){
                   List<Map> maps=(List<Map>)Json.fromJson(spec);
                   for (Map map: maps) {
                       String specValueId=(String)map.get("spec_value_id");
                        if(!Arrays.asList(specArr).contains(specValueId)){
                            flag=false;
                            continue;
                        }
                   }
               }
               if(flag){
                   getSku=goodsProduct.getSku();
                   break;
               }
            }
        }else{
            return getSku;
        }
        return getSku;
    }
}
