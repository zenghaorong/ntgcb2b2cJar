package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.modules.models.*;
import com.aebiz.app.dec.modules.services.DecComponentClassService;
import com.aebiz.app.dec.modules.services.DecComponentResourceService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.services.DecComponentService;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DecComponentServiceImpl extends BaseServiceImpl<Dec_component> implements DecComponentService {

    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    DecComponentClassService decComponentClassService;

    @Autowired
    DecComponentResourceService decComponentResourceService;

    /**
     * DataTable Page
     *
     * @param length   页大小
     * @param start    start
     * @param draw     draw
     * @param orders   排序
     * @param columns  字段
     * @param cnd      查询条件
     * @param linkName 关联查询
     * @return
     */
    @Override
    public NutMap data(String coVersionType,String coId,String coName,String coClass,int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName) {
        NutMap re = new NutMap();
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        Pager pager = new OffsetPager(start, length);
        re.put("recordsFiltered", this.dao().count(this.getEntityClass(), cnd));
        if(Strings.isNotBlank(coId)){
            cnd.and("compId","=",coId);
        }
        if(Strings.isNotBlank(coClass)){
            cnd.and("compCategoryUuid","=",coClass);
        }
        if(Strings.isNotBlank(coName)){
            cnd.and("compName","like",coName+"%");
        }
        if(Strings.isNotBlank(coVersionType)){
            cnd.and("versionType","=",coVersionType);
        }
        List<Dec_component> list = this.query(cnd, pager);
        if(list !=null && list.size()>0){
            for(int i = 0;i < list.size(); i ++ ){
                String compCategoryUuid=list.get(i).getCompCategoryUuid();
                Dec_component_class model=decComponentClassService.fetch(compCategoryUuid);
                list.get(i).setCompCategoryUuid(model.getCategoryName());
                list.set(i, list.get(i));
            }
        }
        if (!Strings.isBlank(linkName)) {
            this.dao().fetchLinks(list, linkName);
        }
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }

    public List<Object> getComClass(String versionType){
        //查询所有可用的组件、并按名字排序
        String str = "SELECT a.* FROM dec_component a,dec_component_class c " +
                " WHERE a.compCategoryUuid=c.id and a.versionType=@versionType and a.disabled=@flag order by c.categoryName asc";
        Sql sql=Sqls.create(str).setParam("flag",DecorateCommonConstant.DECORATE_USING_YES).setParam("versionType",versionType);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(dao().getEntity(Dec_component.class));
        dao().execute(sql);
        List<Dec_component> componentList=sql.getList(Dec_component.class);
        List<Object>  compList=new ArrayList<Object>();
        if(componentList !=null && componentList.size()>0){
            for(int i=0;i<componentList.size();i++){
                Object[] obj=new Object[2];
                Dec_component_class classModel=decComponentClassService.fetch(componentList.get(i).getCompCategoryUuid());
                if(classModel !=null){
                    obj[0]=classModel.getCategoryName();
                    obj[1]=componentList.get(i);
                    compList.add(obj);
                }
            }
        }
        return compList;
    }

    /**
     * 获取所有可用的组件列表
     *
     * @return
     */
    @Override
    public List<CompGroupModel> getUsableComps(String contextPath,String versionType) {
        List<Object> list=getComClass(versionType);
        List<CompGroupModel> compGroupList = null;
        if (list != null && list.size() > 0) {
            compGroupList = new ArrayList<CompGroupModel>();
            List<Dec_component> components = new ArrayList<Dec_component>();
            for (int i = 0; i < list.size(); i++) {
                Object[] objects = (Object[]) list.get(i);
                Object[] nextObjects = null;

                if (i + 1 != list.size()) {
                    nextObjects =  (Object[]) list.get(i+1);
                }

                String categoryName = (String) objects[0];
                Dec_component componentsModel = (Dec_component) objects[1];
                Dec_component_resource resource = decComponentResourceService
                        .getComponentResource(componentsModel.getId(),DecorateCommonConstant.COMPONENT_RESOURCETYE_HTML);
                if (resource != null && !Strings.isEmpty(resource.getResourceKey())) {
                    String compHtml = decComponentResourceService.getRedisResourceBytesByKey(resource.getResourceKey());
                    compHtml = compHtml.replaceAll("\\$\\{contextPath\\}", contextPath);
                    componentsModel.setCompHtml(compHtml);
                }

                components.add(componentsModel);
                if (nextObjects != null) {
                    String nextCategoryName = (String) nextObjects[0];
                    if (categoryName.equals(nextCategoryName)) {
                        continue;
                    }
                }

                CompGroupModel compGroupModel = new CompGroupModel();
                compGroupModel.setCategoryName(categoryName);
                compGroupModel.setComponents(components);
                compGroupList.add(compGroupModel);
                components = new ArrayList<Dec_component>();
            }
        }
        return compGroupList;
    }
    /**
     * 根据组件编号获取组件对象
     */
    @Override
    public Dec_component getComponentsModelByCompId(String compId) {
        Dec_component componentModle= this.fetch(Cnd.where("compId","=",compId));

        if (componentModle != null) {
            return componentModle;
        }
        return null;
    }


    /**
     * 检查组件编号是否重复
     */
    @Override
    public boolean isCompIdExist(String compId) {
        Dec_component compModel=this.fetch(Cnd.where("compId","=",compId));
        if(compModel ==null){
            return true;
        }
        return false;
    }

    /**
     * 根据组件id去删除组件和组件注册数据
     */
    @Transactional
    public void deleteComps(String[] ids){
        List<String> compUuids=new ArrayList<String>();
        Collections.addAll(compUuids,ids);
        this.delete(ids);
        decComponentResourceService.deleteResources(compUuids);
    }

    /**
     * 根据组件id去删除组件和组件注册数据
     */
    @Transactional
    public void deleteComps(String id){
        this.delete(id);
        List<String> compUuid=new ArrayList<String>();
        compUuid.add(id);
        decComponentResourceService.deleteResources(compUuid);
    }
}
