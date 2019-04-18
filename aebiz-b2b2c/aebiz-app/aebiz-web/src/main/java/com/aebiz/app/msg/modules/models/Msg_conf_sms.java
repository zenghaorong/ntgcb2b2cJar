package com.aebiz.app.msg.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 短信平台配置表(只可启用一个)
 * Created by wizzer on 2017/2/21.
 */
@Table("msg_conf_sms")
public class Msg_conf_sms extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("平台名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String name;

    @Column
    @Comment("服务地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String url;

    @Column
    @Comment("接口appkey")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String appkey;

    @Column
    @Comment("接口secret")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String secret;

    @Column
    @Comment("短信签名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String signname;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("是否需设置模板")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean hasTpl;

    @Many(field = "smsId")
    private List<Msg_conf_sms_tpl> smsTpls;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSignname() {
        return signname;
    }

    public void setSignname(String signname) {
        this.signname = signname;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isHasTpl() {
        return hasTpl;
    }

    public void setHasTpl(boolean hasTpl) {
        this.hasTpl = hasTpl;
    }

    public List<Msg_conf_sms_tpl> getSmsTpls() {
        return smsTpls;
    }

    public void setSmsTpls(List<Msg_conf_sms_tpl> smsTpls) {
        this.smsTpls = smsTpls;
    }
}
