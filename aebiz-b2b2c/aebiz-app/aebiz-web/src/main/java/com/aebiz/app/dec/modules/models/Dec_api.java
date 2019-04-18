package com.aebiz.app.dec.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口管理
 * Created by yewei on 2017/4/13.
 */
@Table("dec_api")
public class Dec_api  extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("接口名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private  String interfaceName;

    @Column
    @Comment("接口分组uuid")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private  String groupUuid;

    @Column
    @Comment("接口描述")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private  String description;

    @Column
    @Comment("分组名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String groupName;

    @Comment("接口参数")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private List<Dec_api_config_params> paramList = new ArrayList<Dec_api_config_params>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public List<Dec_api_config_params> getParamList() {
        return paramList;
    }

    public void setParamList(List<Dec_api_config_params> paramList) {
        this.paramList = paramList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInterfaceUrl() {
        return interfaceUrl;
    }

    public void setInterfaceUrl(String interfaceUrl) {
        this.interfaceUrl = interfaceUrl;
    }

    @Column
    @Comment("接口访问路径")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private  String interfaceUrl;
}
