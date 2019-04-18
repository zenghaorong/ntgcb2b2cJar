package com.aebiz.app.goods.modules.services;

import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;

import java.util.List;
import java.util.Map;

public interface GoodsService extends BaseService<Goods_main> {

    NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, String linkName, Goods_main goods, NutMap fieldMap);

    void add(Goods_main goods);

    void save(Goods_main goods);

    /**
     *  单条伪删除
     * @param id 商品ID
     * @param storeId 商户ID
     */
    void vDeleteGoods(String id, String storeId);

    /**
     *  批量伪删除
     * @param ids 商品IDS
     * @param storeId 商户ID
     */
    void vDeleteGoods(String[] ids, String storeId);

    /**
     *  上下架
     * @param id 商品ID
     * @param sale true 上架 false 下架
     * @param storeId 商户ID
     */
    void updown(String id, boolean sale, String storeId);

    /**
     *  批量上下架
     * @param ids 商品IDS
     * @param sale true 上架 false 下架
     * @param storeId 商户ID
     */
    void updowns(String[] ids, boolean sale, String storeId);

    /**
     *  保存商品标签关联关系
     * @param ids 商品IDS
     * @param tagIds 标签IDS
     */
    void saveTagLink(String[] ids, String[] tagIds);

    /**
     * 违规下架
     * @param ids
     * @param reason
     */
    void markBreakrule(String[] ids, String reason);

    /**
     * 商品tab列表统计
     * @param storeId
     * @return
     */
    Map getStatisticMap(String storeId);

    /**
     *  审核
     * @param goodsId 商品ID
     * @param checkStatus 审核状态
     * @param reason 意见
     */
    void check(String goodsId, Integer checkStatus, String reason);

    /**
     *  批量审核
     * @param goodsIds 商品ID
     * @param checkStatus 审核状态
     * @param comment 意见
     */
    void check(String[] goodsIds, Integer checkStatus, String comment);

    /**
     * 是否存在重复的sku
     * @param goods
     * @return
     */
    boolean isDuplicatedSku(Goods_main goods);
}
