package com.aebiz.app.order.modules.services;

import com.aebiz.app.order.modules.models.Order_goods_feedback;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.lang.util.NutMap;

import java.util.List;

public interface OrderGoodsFeedbackService extends BaseService<Order_goods_feedback>{

    //获取评论数量
    int getFeedbackNumBySku(String sku);

    /**
     * 获取好评数
     */
    int getAppCountByType(String sku,String state);

    /**
     * 获取评价
     *
     * @param sku
     * @param type
     *            全部--0;好评--1;中评--2;差评--3
     * @return
     */
     Pagination getAppByType(String sku, String type, int start, int showPage);

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
     NutMap selfOrderGoodsFeedbackData(int length, int start, int draw, List<DataTableOrder> orders,
                 List<DataTableColumn> columns, NutMap fieldMap);
}
