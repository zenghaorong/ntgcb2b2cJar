package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.order.modules.models.Order_goods_feedback;
import com.aebiz.app.order.modules.services.OrderGoodsFeedbackService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Daos;
import org.nutz.lang.util.NutMap;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@CacheConfig(cacheNames = "goodsCache")
public class OrderGoodsFeedbackServiceImpl
        extends BaseServiceImpl<Order_goods_feedback> implements OrderGoodsFeedbackService {

    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 根据sku获取货品的评论数
     * @param sku
     * @return
     */
    @Cacheable(key = "'FEEDBACK'+#sku")
    public int getFeedbackNumBySku(String sku){
        return this.count(Cnd.where("delFlag","=",false).and("sku","=",sku));
    }

    /**
     * 清除评论缓存
     * @param sku
     */
    @Async
    public void clearFeedBack(String sku) {
        this.clearGetFeedbackNum(sku);
    }

    @CacheEvict(key = "'FEEDBACK'+#sku")
    public void clearGetFeedbackNum(String sku) {

    }

    /**
     * 获取好评数
     * 1 好评 2 中评 3差评 4晒单
     */
    public int getAppCountByType(String sku,String state){
        StringBuffer str=new StringBuffer("select count(1) from order_goods_feedback o where o.sku=@sku");
        if("1".equals(state)){
            str.append(" and (o.appScore=4 or o.appScore=5)");
        }else if("2".equals(state)){
            str.append(" and (o.appScore=3)");
        }else if("3".equals(state)){
            str.append(" and (o.appScore=1 or o.appScore=2)");
        }else if("4".equals(state)){
            str.append(" and (o.feedImage !='')");
        }
        Sql sql=Sqls.create(str.toString());
        int appCount=this.count(sql.setParam("sku",sku));
        return appCount;
    }


    /**
     * 获取评价
     *
     * @param sku
     * @param type 全部--0;好评--1;中评--2;差评--3
     * @return
     */
    public Pagination getAppByType(String sku, String type, int start, int showPage){
        StringBuffer str=new StringBuffer("select * from order_goods_feedback o where o.sku='"+sku+"'");
        if("1".equals(type)){
            str.append("and (o.appScore=4 or o.appScore=5)");
        }else if("2".equals(type)){
            str.append("and (o.appScore=3)");
        }else if("3".equals(type)){
            str.append("and (o.appScore=1 or o.appScore=2)");
        }else if("4".equals(type)){
            str.append("and (o.feedImage !='')");
        }
        str.append(" order by o.feedAt desc ");
        Sql sql=Sqls.create(str.toString());
        Pagination p=this.listPage(start,showPage,sql);
        return p;
    }

    /**
     * 自营商户评价管理列表查询数据方法
     * @param length
     * @param start
     * @param draw
     * @param orders
     * @param columns
     * @param fieldMap
     * @return
     */
    public NutMap selfOrderGoodsFeedbackData(int length, int start, int draw, List<DataTableOrder> orders,
                List<DataTableColumn> columns, NutMap fieldMap){
        StringBuffer stringBuffer = new StringBuffer("SELECT ");
        stringBuffer.append(" store.storeName, CONCAT_WS('',goods.name,product.name) as productName,account.loginname, feedback.*  ");
        stringBuffer.append("FROM order_goods_feedback feedback  ");
        stringBuffer.append("LEFT JOIN store_main store on store.id = feedback.storeId ");
        stringBuffer.append("LEFT JOIN goods_main goods on goods.id = feedback.goodsId ");
        stringBuffer.append("LEFT JOIN goods_product product on product.id = feedback.productId ");
        stringBuffer.append("LEFT JOIN account_user account on account.accountId = feedback.accountId ");
        stringBuffer.append("WHERE 1 = 1 ");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        NutMap valuesMap = NutMap.NEW();
        if (fieldMap.containsKey("feedOpen")) {
            stringBuffer.append("and feedback.feedOpen = @feedOpen ");
            valuesMap.put("feedOpen", fieldMap.getBoolean("feedOpen"));
        }
        if(fieldMap.containsKey("storeId")){
            stringBuffer.append("and feedback.storeId = @storeId ");
            valuesMap.put("storeId", fieldMap.getBoolean("storeId"));
        }
        if (fieldMap.containsKey("name")) {
            stringBuffer.append("AND (feedback.orderId LIKE @name ");
            stringBuffer.append(" OR store.storeName LIKE @name OR account.loginname LIKE @name ");
            stringBuffer.append(" OR CONCAT_WS('',goods.name,product.name) LIKE @name ) ");
            valuesMap.put("name", "%" + fieldMap.getString("name") + "%");
        }
        if (fieldMap.containsKey("startAt")) {
            stringBuffer.append("and feedback.feedAt >= @startAt ");
            valuesMap.put("startAt", fieldMap.getInt("startAt"));
        }
        if (fieldMap.containsKey("endAt")) {
            stringBuffer.append("and feedback.feedAt <= @endAt ");
            valuesMap.put("endAt", fieldMap.getInt("endAt"));
        }

        //组装countSql
        Sql countSql = Sqls.create(stringBuffer.toString());
        countSql.params().putAll(valuesMap);

        //排序字段
        stringBuffer.append(" ORDER BY ");
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                stringBuffer.append(Sqls.escapeSqlFieldValue(col.getData()).toString()).append(" ").append(order.getDir()).append(", ");
            }
        }
        stringBuffer.append(" feedback.opAt desc ");//默认排序

        //组装orderSql
        Sql orderSql = Sqls.create(stringBuffer.toString());
        orderSql.params().putAll(valuesMap);

        NutMap returnMap = new NutMap();
        Pager pager = new OffsetPager(start, length);
        pager.setRecordCount((int) Daos.queryCount(this.dao(), countSql));
        orderSql.setPager(pager);
        orderSql.setCallback(Sqls.callback.maps());
        this.dao().execute(orderSql);
        returnMap.put("recordsFiltered", Integer.valueOf(pager.getRecordCount()));
        returnMap.put("data", orderSql.getList(Record.class));
        returnMap.put("draw", Integer.valueOf(draw));
        returnMap.put("recordsTotal", Integer.valueOf(length));
        return returnMap;
    }

}
