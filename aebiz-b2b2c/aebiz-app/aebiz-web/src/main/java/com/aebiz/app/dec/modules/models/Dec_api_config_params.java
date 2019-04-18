package com.aebiz.app.dec.modules.models;

import org.nutz.dao.entity.annotation.*;
import com.aebiz.baseframework.base.model.BaseModel;
import java.io.Serializable;

/**
 * Created by yewei on 2017/4/14.
 */
@Table("dec_api_config_params")
public class Dec_api_config_params extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("接口uuid")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String interfaceUuid ;

    @Column
    @Comment("参数名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String paramName;

    @Column
    @Comment("参数描述")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String paramDescribe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterfaceUuid() {
        return interfaceUuid;
    }

    public void setInterfaceUuid(String interfaceUuid) {
        this.interfaceUuid = interfaceUuid;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamDescribe() {
        return paramDescribe;
    }

    public void setParamDescribe(String paramDescribe) {
        this.paramDescribe = paramDescribe;
    }

    public String getWhetherMust() {
        return whetherMust;
    }

    public void setWhetherMust(String whetherMust) {
        this.whetherMust = whetherMust;
    }

    @Column
    @Comment("是否必须")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String whetherMust;
}
