package com.aebiz.app.msg.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 系统消息表
 * Created by wizzer on 2017/2/21.
 */
@Table("msg_info")
public class Msg_info extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("消息名称")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String name;

    @Column
    @Comment("消息代码")//消息代码的唯一性
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String nameCode;

    @Column
    @Comment("支持站内信")
    private boolean supportMSG;

    @Column
    @Comment("是否禁用站内信")
    private boolean disabledMSG;

    @Column
    @Comment("是否支持短信")
    private boolean supportSMS;

    @Column
    @Comment("是否禁用短信")
    private boolean disabledSMS;

    @Column
    @Comment("是否支持EMAIL")
    private boolean supportEmail;

    @Column
    @Comment("是否禁用EMAIL")
    private boolean disabledEmail;

    @Column
    @Comment("排序字段")
    @Prev({
            @SQL(db = DB.MYSQL, value = "SELECT IFNULL(MAX(location),0)+1 FROM msg_info"),
            @SQL(db = DB.ORACLE, value = "SELECT COALESCE(MAX(location),0)+1 FROM msg_info")
    })
    private Integer location;

    @Many(field = "infoId")
    private List<Msg_info_param> paramList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSupportMSG() {
        return supportMSG;
    }

    public void setSupportMSG(boolean supportMSG) {
        this.supportMSG = supportMSG;
    }

    public boolean isDisabledMSG() {
        return disabledMSG;
    }

    public void setDisabledMSG(boolean disabledMSG) {
        this.disabledMSG = disabledMSG;
    }

    public boolean isSupportSMS() {
        return supportSMS;
    }

    public void setSupportSMS(boolean supportSMS) {
        this.supportSMS = supportSMS;
    }

    public boolean isDisabledSMS() {
        return disabledSMS;
    }

    public void setDisabledSMS(boolean disabledSMS) {
        this.disabledSMS = disabledSMS;
    }

    public boolean isSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(boolean supportEmail) {
        this.supportEmail = supportEmail;
    }

    public boolean isDisabledEmail() {
        return disabledEmail;
    }

    public void setDisabledEmail(boolean disabledEmail) {
        this.disabledEmail = disabledEmail;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public List<Msg_info_param> getParamList() {
        return paramList;
    }

    public void setParamList(List<Msg_info_param> paramList) {
        this.paramList = paramList;
    }

    public String getNameCode() {
        return nameCode;
    }

    public void setNameCode(String nameCode) {
        this.nameCode = nameCode;
    }
}
