package com.aebiz.app.dec.modules.services;

import com.aebiz.app.dec.modules.models.CompGroupModel;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.dec.modules.models.Dec_component;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import javax.xml.soap.SAAJResult;
import java.util.List;

public interface DecComponentService extends BaseService<Dec_component>{
    public NutMap data(String coVersionType,String coId,String coName,String coClass,int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName);
    public List<CompGroupModel> getUsableComps(String contextPath,String versionType);


    /**
     * 根据组件id去删除组件和组件注册数据
     */
    public void deleteComps(String[] ids);

    /**
     * 根据组件id去删除组件和组件注册数据
     */
    public void deleteComps(String id);


    /**
     * 根据组件编号获取组件对象
     */
    public Dec_component getComponentsModelByCompId(String compId);

    /**
     * 检查组件编号是否重复
     */
    public boolean isCompIdExist(String compId);
}
