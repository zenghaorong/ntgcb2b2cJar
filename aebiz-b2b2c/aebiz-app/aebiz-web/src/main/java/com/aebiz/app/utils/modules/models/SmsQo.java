package com.aebiz.app.utils.modules.models;

/**
 * 短信发送实体类
 */
public class SmsQo {

    private String userName; //用户名
    private String userPwd; //密码
    private String destAddr; //	目的手机号码，多个以英文“,”隔开，最多300个
    private String content; //	短信内容，最多200字
    private String srcAddr; //	开户企业接入码
    private String exteCode; //	企业扩展码，最大3位，不够3位前面补0
    private String clientType; //开发语言：11-java 12-php 13-python 14-C#/net 15-C++


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(String srcAddr) {
        this.srcAddr = srcAddr;
    }

    public String getExteCode() {
        return exteCode;
    }

    public void setExteCode(String exteCode) {
        this.exteCode = exteCode;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}