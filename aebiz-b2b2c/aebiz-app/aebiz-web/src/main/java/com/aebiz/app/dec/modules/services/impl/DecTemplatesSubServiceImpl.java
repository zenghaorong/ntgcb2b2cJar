package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.app.dec.commons.utils.DecorateCacheConstant;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.modules.services.DecTemplatesPagesService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_templates_sub;
import com.aebiz.app.dec.modules.services.DecTemplatesSubService;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.redis.RedisService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.LinkedList;
import java.util.List;


@Service
public class DecTemplatesSubServiceImpl extends BaseServiceImpl<Dec_templates_sub> implements DecTemplatesSubService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    private RedisService redisService;
    @Autowired
    private DecTemplatesPagesService decTemplatesPagesService;
    /**
     * DataTable Page
     *@param id
     * @param length   页大小
     * @param start    start
     * @param draw     draw
     * @param orders   排序
     * @param columns  字段
     * @param cnd      查询条件
     * @param linkName 关联查询
     * @return
     */
    public NutMap data(String id,int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName){
        NutMap re = new NutMap();
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        Pager pager = new OffsetPager(start, length);
        re.put("recordsFiltered", this.dao().count(this.getEntityClass(), cnd));
        List<Dec_templates_sub>  list= this.query(cnd.where("pageUuid","=",id));
        if (!Strings.isBlank(linkName)) {
            this.dao().fetchLinks(list, linkName);
        }
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }

    /**
     * 获取当前正在使用的页面保存的key和版本号
     */
    @Override
    public Dec_templates_sub getResourceOfCurrentPage(String pageUuid) {
        Dec_templates_sub subModel=this.fetch(Cnd.where("pageUuid","=",pageUuid).and("disabled","=", DecorateCommonConstant.DECORATE_USING_YES));
        if(subModel !=null){
            return subModel;
        }
        return null;
    }
    /**
     * 获取当前正在使用的页面保存的key和版本号
     */
    @Override
    public Dec_templates_sub getResourceArrOfCurrentPage(String pageUuid) {
        Dec_templates_sub subModel= this.fetch(Cnd.where("pageUuid","=",pageUuid).and("disabled","=",DecorateCommonConstant.DECORATE_USING_YES));
        return subModel;
    }
    //====================================added by ly 性能优化 start================================================//
    /**
     * 从缓存中获取当前正在使用的页面保存的key和版本号
     */
    public Dec_templates_sub getCurrentUsingPageFromCache(String pageUuid) {
        String key = DecorateCacheConstant.DECORATEPLATFORM_SUBPAGE_USING+pageUuid;
        Dec_templates_sub page = null;
        byte[] resourceBytes = null;
        try (Jedis jedis = redisService.jedis()) {
            resourceBytes=jedis.get(key.getBytes());
            if(resourceBytes !=null){
                page=Lang.fromBytes(resourceBytes,Dec_templates_sub.class);
            }
            if(page== null){
                page = getResourceArrOfCurrentPage(pageUuid);
                jedis.set(key.getBytes(),Lang.toBytes(page));
            }
        }
        return page;

    }

    //====================================added by ly 性能优化 end================================================//
    /**
     * 根据页面id(pageUuid)获取版本信息
     */
    public List<Dec_templates_sub> getSubPagesByPageUuid(String pageUuid){
        return this.query(Cnd.where("pageUuid","=",pageUuid));
    }

    /**
     * 切换页面版本
     */
    @Override
    public void switchPageVersion(String pageUuid, String subPageUuid) {
        Dec_templates_sub model = this.getResourceOfCurrentPage(pageUuid);

        if (model != null && !subPageUuid.equals(model.getId())) {
            updateOriginalUsingSubPageState(pageUuid);

            updateSubPageUsed(subPageUuid);

            //====================================added by  ly at 2017-2-14  性能优化start================================================//
            //修改后将正在使用的页面存入缓存
            updateCacheCurrentUsingPage(pageUuid);
            //====================================added by  ly at 2017-2-14  性能优化end================================================//
        }

    }

    public void updateOriginalUsingSubPageState(String pageUuid) {
        Dec_templates_sub subModel=this.fetch(Cnd.where("pageUuid","=",pageUuid).and("disabled","=",DecorateCommonConstant.DECORATE_USING_YES));
        subModel.setDisabled( DecorateCommonConstant.DECORATE_USING_NO);
        this.update(subModel);
    }


    /**
     * 更新子页面的启用状态为已启用
     */
    public void updateSubPageUsed(String uuid) {
        Dec_templates_sub subModle=this.fetch(uuid);
        subModle.setDisabled(DecorateCommonConstant.DECORATE_USING_YES);
        this.update(subModle);
    }

    /**
     * 更新缓存中 正在使用的版本
     * @param pageUuid
     */
    public void updateCacheCurrentUsingPage(String pageUuid){
        Dec_templates_sub page = getResourceArrOfCurrentPage(pageUuid);
        String key = DecorateCacheConstant.DECORATEPLATFORM_SUBPAGE_USING+pageUuid;
        if(page!=null){
            try (Jedis jedis = redisService.jedis()) {
                jedis.set(key.getBytes(),Lang.toBytes(page));
            }
        }
    }


    /**
     * 将页面保存为新版本
     */
    @Override
    public void saveAsNewVersion(String templateUuid, String pageUuid, String versionDescribe) {
        Dec_templates_sub originalSubPagesModel = this.getResourceOfCurrentPage(pageUuid);
        originalSubPagesModel.setDisabled(DecorateCommonConstant.DECORATE_USING_NO);
        this.update(originalSubPagesModel);

        // 获取页面保存的最高版本号
        String highestVersionNo = getHighestVersionNoOfPage(pageUuid);
        double versionNo = Double.parseDouble(highestVersionNo) + 1;
        String resourceKey = templateUuid + "_" + pageUuid + versionNo;
        Dec_templates_sub subPagesModel = new Dec_templates_sub();
        subPagesModel.setPageUuid(pageUuid);
        subPagesModel.setDisabled(DecorateCommonConstant.DECORATE_USING_YES);
        subPagesModel.setVersionNo(String.valueOf(versionNo));
        subPagesModel.setDescription(versionDescribe);
        subPagesModel.setResourceKey(resourceKey);
        subPagesModel.setCreateTime((int)(System.currentTimeMillis() / 1000));

        this.insert(subPagesModel);

        if (originalSubPagesModel != null) {
            String originalResourceKey = originalSubPagesModel.getResourceKey();
            decTemplatesPagesService.saveResourceToRedisByKeys(originalResourceKey, resourceKey);
        }

        //====================================added by  ly at 2017-2-14  性能优化start================================================//
        //更新缓存
        updateCacheCurrentUsingPage(pageUuid);
        //====================================added by  ly at 2017-2-14  性能优化start================================================//

    }


    /**
     * 获取页面保存的最高版本号
     */
    public String getHighestVersionNoOfPage(String pageUuid) {
        List<Dec_templates_sub> subList=this.query(Cnd.where("pageUuid","=",pageUuid).asc("createTime"));
        if(subList !=null && subList.size()>0){
            String versionNo = subList.get(0).getVersionNo();
            return versionNo;
        }
        return "";
    }


    /**
     * 获取模板下所有页面资源key集合
     */
    @Override
    public List<String> getSubPageResourceKeysByTemplateUuid(String templateUuid) {
        String str="select o.resourceKey from dec_templates_sub o where o.resourceKey like @resourceKey";
        Sql sql = Sqls.create(str).setParam("resourceKey",templateUuid + "%");
        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                List<String> list = new LinkedList<String>();
                while (rs.next())
                    list.add(rs.getString("resourceKey"));
                return list;
            }
        });
        dao().execute(sql);
        List<String> list=sql.getList(String.class);
        return list;
    }

    /**
     * 删除模板下的所有页面资源
     *
     * @param templateUuid
     */
    public void deleteSubPagesByTemplateUuid(String templateUuid) {
        String str="delete from Dec_templates_sub where resourceKey like @resourceKey";
        Sql sql=Sqls.create(str).setParam("resourceKey",templateUuid+"%");
        dao().execute(sql);
    }


    /**
     * 根据对应的page页面的id去删除版本号
     *
     * @param
     */
    public void deleteByPageUuid(String pageUuid){
        String str ="delete from Dec_templates_sub where pageUuid =@pageUuid";
        Sql sql=Sqls.create(str).setParam("pageUuid",pageUuid);
        dao().execute(sql);
    }
}
