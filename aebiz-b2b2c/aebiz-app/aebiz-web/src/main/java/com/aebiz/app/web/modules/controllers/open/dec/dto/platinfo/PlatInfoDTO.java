package com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo;

/**
 * 平台信息相关DTO
 *
 * Created by Aebiz_yjq on 2017/1/11.
 */
public class PlatInfoDTO {

    /*平台名称*/
    private String platName;

    /*当前登录的用户的名称*/
    private String loginName;

    public PlatInfoDTO() {
    }

    public PlatInfoDTO(String platName, String loginName) {
        this.platName = platName;
        this.loginName = loginName;
    }

    public String getPlatName() {
        return platName;
    }

    public void setPlatName(String platName) {
        this.platName = platName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String toString() {
        return "PlatInfoDTO{" +
                "platName='" + platName + '\'' +
                ", loginName='" + loginName + '\'' +
                '}';
    }
}

