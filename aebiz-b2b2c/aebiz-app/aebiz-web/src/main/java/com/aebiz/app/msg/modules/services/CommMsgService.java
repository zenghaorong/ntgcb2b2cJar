package com.aebiz.app.msg.modules.services;

import org.nutz.lang.util.NutMap;

/**
 * Created by ThinkPad on 2017/7/13.
 * 公共的发消息的接口[根据后台的消息配置进行发送]
 */
public interface CommMsgService {

    /**
     * 消息中心接口（根据发送类型）
     */
    void sendMsg(String msgCode, String userId, String sendTo, String sendForm, String sendTitle, String sendType, String receiveId, String receiveName, NutMap msgParam);


    /**
     * 消息中心接口（根据发送类型、并加上是否需要消息记录）
     */
    void sendMsg(String msgCode, String userId, String sendTo, String sendForm, String sendTitle, String sendType, String receiveId, String receiveName, NutMap msgParam, boolean isRecord);

    /**
     * 消息中心接口（不区分发送类型）
     */
    void sendAllMsg(String msgCode, String userId, String sendTo, String sendForm, String sendTitle, String receiveId, String emailName, String mobile, NutMap msgParam);

    /**
     * 发送短信验证码
     *
     * @param code 消息代码
     * @param mobile   手机号
     * @param captcha  验证码
     */
    boolean sendSMSCaptcha(String code, String mobile, String captcha);

    /**
     * 发送邮箱验证码
     *
     * @param code 消息代码
     * @param email   邮箱
     * @param captcha  验证码
     */
    boolean sendEmailCaptcha(String code, String email, String captcha);
}
