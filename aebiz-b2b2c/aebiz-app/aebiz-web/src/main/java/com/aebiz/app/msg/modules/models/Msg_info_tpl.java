package com.aebiz.app.msg.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 系统消息模板表
 * Created by wizzer on 2017/2/21.
 */
@Table("msg_info_tpl")
public class Msg_info_tpl extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("消息ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String infoId;

    @Column
    @Comment("站内信模板")
    @ColDefine(type = ColType.TEXT)
    private String tplMSG;

    @Column
    @Comment("短信模板")
    @ColDefine(type = ColType.TEXT)
    private String tplSMS;

    @Column
    @Comment("电子邮件模板")
    @ColDefine(type = ColType.TEXT)
    private String tplEmail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getTplMSG() {
        return tplMSG;
    }

    public void setTplMSG(String tplMSG) {
        this.tplMSG = tplMSG;
    }

    public String getTplSMS() {
        return tplSMS;
    }

    public void setTplSMS(String tplSMS) {
        this.tplSMS = tplSMS;
    }

    public String getTplEmail() {
        return tplEmail;
    }

    public void setTplEmail(String tplEmail) {
        this.tplEmail = tplEmail;
    }
}
