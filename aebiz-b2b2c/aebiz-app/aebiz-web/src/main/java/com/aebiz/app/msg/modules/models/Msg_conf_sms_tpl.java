package com.aebiz.app.msg.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 短信平台模板关联表
 * Created by wizzer on 2017/2/21.
 */
@Table("msg_conf_sms_tpl")
public class Msg_conf_sms_tpl extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("短信平台ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String smsId;

    @Column
    @Comment("系统消息ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String infoId;

    @Column
    @Comment("模板ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String tplId;

    @Column
    @Comment("模板名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String tplName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getTplId() {
        return tplId;
    }

    public void setTplId(String tplId) {
        this.tplId = tplId;
    }

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }
}
