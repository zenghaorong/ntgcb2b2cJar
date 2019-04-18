package com.aebiz.app.msg.modules.services.impl;

import com.aebiz.app.msg.commons.email.EmailService;
import com.aebiz.app.msg.commons.sms.SmsService;
import com.aebiz.app.msg.commons.util.MsgTemplateUtil;
import com.aebiz.app.msg.modules.models.*;
import com.aebiz.app.msg.modules.services.CommMsgService;
import com.aebiz.app.msg.modules.services.MsgInfoService;
import com.aebiz.app.msg.modules.services.MsgInfoTplService;
import com.aebiz.app.msg.modules.services.MsgSendMsgService;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/7/13.
 */
@Service
public class CommMsgServiceImpl implements CommMsgService {

    private Log log = Logs.get();

    @Autowired
    private MsgSendMsgService msgSendMsgService;

    @Autowired
    private MsgInfoService msgInfoService;

    @Autowired
    private MsgInfoTplService msgInfoTplService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    /**
     * 发送代码消息接口
     *
     * @param msgCode     消息代码
     * @param userId      当前发送人的用户Id
     * @param sendTo      接收人的所属平台
     * @param sendForm    发送人的所属平台
     * @param sendTitle   发送的标题
     * @param sendType    发送类型 【MSG EMAIL SMS】
     * @param receiveId   接收人的账号ID
     * @param receiveName 接收人的名称{此参数：发送邮件或者短信此字段必传,站内信可不传}
     */
    @Transactional
    public void sendMsg(String msgCode, String userId, String sendTo, String sendForm, String sendTitle, String sendType, String receiveId, String receiveName, NutMap msgParam) {
        //判断code
        if (Strings.isBlank(msgCode)) {
            log.debug(msgCode + " 消息代码为空");
            return;
        }
        Msg_info msg_info = msgInfoService.fetch(Cnd.where("nameCode", "=", msgCode).and("delFlag", "=", false));
        if (Lang.isEmpty(msg_info)) {
            log.debug(msgCode + " 消息代码不存在");
            return;
        }
        //查询模板
        Msg_info_tpl msgInfoTpl = msgInfoTplService.fetch(Cnd.where("infoId", "=", msg_info.getId()));
        //根据sendType进行发送消息
        send(sendType, msg_info, msgInfoTpl, userId, sendTitle, sendTo, sendForm, receiveId, receiveName, msgParam);
    }

    /**
     * 发送代码消息接口
     *
     * @param msgCode     消息代码
     * @param userId      当前发送人的用户Id
     * @param sendTo      接收人的所属平台
     * @param sendForm    发送人的所属平台
     * @param sendTitle   发送的标题
     * @param receiveId   接收人的账号ID
     * @param receiveName 接收人的名称{此参数：发送邮件或者短信此字段必传,站内信可不传}
     * @param msgParam    消息参数
     * @param isRecord    是否做消息记录
     */
    @Transactional
    public void sendMsg(String msgCode, String userId, String sendTo, String sendForm, String sendTitle, String sendType, String receiveId, String receiveName, NutMap msgParam, boolean isRecord) {
        //判断code
        Map<String, String> resultMap = new HashMap<>();
        if (Strings.isBlank(msgCode)) {
            log.debug(msgCode + " 消息代码为空");
            return;
        }
        Msg_info msg_info = msgInfoService.fetch(Cnd.where("nameCode", "=", msgCode).and("delFlag", "=", false));
        if (Lang.isEmpty(msg_info)) {
            log.debug(msgCode + " 消息代码不存在");
            return;
        }
        //查询模板
        Msg_info_tpl msgInfoTpl = msgInfoTplService.fetch(Cnd.where("infoId", "=", msg_info.getId()));
        //根据sendType进行发送消息
        send(sendType, msg_info, msgInfoTpl, userId, sendTitle, sendTo, sendForm, receiveId, receiveName, msgParam, isRecord);
    }

    /**
     * 发生消息代码的所有消息
     *
     * @param msgCode   消息代码
     * @param userId    当前发送人的用户Id
     * @param sendTo    接收人的所属平台
     * @param sendForm  发送人的所属平台
     * @param sendTitle 发送的标题
     * @param receiveId 接收人的账号ID
     * @param emailName 邮箱地址
     * @param mobile    手机号码
     * @param msgParam  消息占位参数
     */
    @Transactional
    public void sendAllMsg(String msgCode, String userId, String sendTo, String sendForm, String sendTitle, String receiveId, String emailName, String mobile, NutMap msgParam) {
        //判断code
        if (Strings.isBlank(msgCode)) {
            log.debug(msgCode + " 消息代码为空");
            return;
        }
        Msg_info msgInfo = msgInfoService.fetch(Cnd.where("nameCode", "=", msgCode).and("delFlag", "=", false));
        if (Lang.isEmpty(msgInfo)) {
            log.debug(msgCode + " 消息代码不存在");
            return;
        }
        //查询模板
        Msg_info_tpl msgInfoTpl = msgInfoTplService.fetch(Cnd.where("infoId", "=", msgInfo.getId()));
        //发送站内信
        send(Msg_send.SendTypeEnum.MSG.getName(), msgInfo, msgInfoTpl, userId, sendTitle, sendTo, sendForm, receiveId, null, msgParam);
        //发送邮件
        send(Msg_send.SendTypeEnum.EMAIL.getName(), msgInfo, msgInfoTpl, userId, sendTitle, sendTo, sendForm, receiveId, emailName, msgParam);
        //发生短信
        send(Msg_send.SendTypeEnum.SMS.getName(), msgInfo, msgInfoTpl, userId, sendTitle, sendTo, sendForm, receiveId, mobile, msgParam);
    }

    /**
     * 发送短信验证码
     *
     * @param code    消息代码
     * @param mobile  手机号
     * @param captcha 验证码
     */
    @Override
    public boolean sendSMSCaptcha(String code, String mobile, String captcha) {
        if (Strings.isEmpty(code) || Strings.isEmpty(mobile) || Strings.isEmpty(captcha)) {
            return false;
        }

        try {
            Cnd cnd = Cnd.where("nameCode", "=", code).and("delFlag", "=", false);
            Msg_info msgInfo = msgInfoService.fetch(cnd);
            if (Lang.isEmpty(msgInfo)) {
                log.debug(code + " 消息代码不存在");
                return false;
            }



            //判断是否支持发送短信
            if (!msgInfo.isSupportSMS()) {
                log.debug(code + "当前消息代码配置 禁用了短信");
                return false;
            }

            //查询模板并替换模板内容
            Msg_info_tpl msgInfoTpl = msgInfoTplService.fetch(Cnd.where("infoId", "=", msgInfo.getId()));
            String smsTempContent = msgInfoTpl.getTplSMS();
            smsTempContent = MsgTemplateUtil.replaceContent(smsTempContent, NutMap.NEW().addv("code", captcha).addv("product", "茗流荟"));
            msgSave("", msgInfo.getId(), "", "", "", "", "", smsTempContent);

            //发送短信
            smsService.send(msgInfo.getId(), mobile, NutMap.NEW().addv("code", captcha).addv("product", "茗流荟"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 发送邮箱验证码
     *
     * @param code    消息代码
     * @param email   邮箱
     * @param captcha 验证码
     */
    @Override
    public boolean sendEmailCaptcha(String code, String email, String captcha) {
        if (Strings.isEmpty(code) || Strings.isEmpty(email) || Strings.isEmpty(captcha)) {
            return false;
        }

        try {
            Cnd cnd = Cnd.where("nameCode", "=", code).and("delFlag", "=", false);
            Msg_info msgInfo = msgInfoService.fetch(cnd);
            if (Lang.isEmpty(msgInfo)) {
                log.debug(code + " 消息代码不存在");
                return false;
            }

            //判断是否支持发送邮件
            if (!msgInfo.isSupportEmail()) {
                log.debug(code +"不支持邮件功能");
                return false;
            }

            //查询模板并替换模板内容
            Msg_info_tpl msgInfoTpl = msgInfoTplService.fetch(Cnd.where("infoId", "=", msgInfo.getId()));
            String smsTempContent = MsgTemplateUtil.replaceContent(msgInfoTpl.getTplEmail(), NutMap.NEW().addv("code", captcha).addv("product", "茗流荟"));

            //发送邮件
            return emailService.send(msgInfo.getId(), email, smsTempContent);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private void send(String sendType, Msg_info msgInfo, Msg_info_tpl msgInfoTpl, String userId, String sendTitle, String sendTo, String sendForm, String receiveId, String receiveName, NutMap msgParam) {
        String msgCode = msgInfo.getNameCode();
        switch (sendType) {
            case "MSG":
                //判断是否发送站内信
                if (!msgInfo.isSupportMSG()) {
                    log.debug(msgCode + "当前消息代码配置 禁用了站内信");
                    return;
                }
                //替换模板内容
                String msgTempContent = msgInfoTpl.getTplMSG();
                msgTempContent = MsgTemplateUtil.replaceContent(msgTempContent, msgParam);
                msgSave(userId, msgInfo.getId(), sendTitle, sendType, sendTo, sendForm, receiveId, msgTempContent);
                break;
            case "EMAIL":
                //判断是否要发送邮件
                if (!msgInfo.isSupportEmail()) {
                    log.debug(msgCode + "当前消息代码配置 禁用了邮件");
                    return;
                }
                //替换模板内容
                String emailTempContent = msgInfoTpl.getTplEmail();
                emailTempContent = MsgTemplateUtil.replaceContent(emailTempContent, msgParam);
                msgSave(userId, msgInfo.getId(), sendTitle, sendType, sendTo, sendForm, receiveId, emailTempContent);
                emailService.send(receiveName, sendTitle, emailTempContent);
                break;
            case "SMS":
                //判断是否发送短信
                if (!msgInfo.isSupportSMS()) {
                    log.debug(msgCode + "当前消息代码配置 禁用了短信");
                    return;
                }
                //替换模板内容
                String smsTempContent = msgInfoTpl.getTplSMS();
                smsTempContent = MsgTemplateUtil.replaceContent(smsTempContent, msgParam);
                msgSave(userId, msgInfo.getId(), sendTitle, sendType, sendTo, sendForm, receiveId, smsTempContent);
                //发送短信
                smsService.send(msgInfo.getId(), receiveName, msgParam);
                break;
            default:

        }
    }

    /**
     * 发送消息方法[邮件和短信可配置是否记录-关联 isRecord参数]
     */
    private void send(String sendType, Msg_info msgInfo, Msg_info_tpl msgInfoTpl, String userId, String sendTitle, String sendTo, String sendForm, String receiveId, String receiveName, NutMap msgParam, boolean isRecord) {
        String msgCode = msgInfo.getNameCode();
        switch (sendType) {
            case "MSG":
                //判断是否发送站内信
                if (!msgInfo.isSupportMSG()) {
                    log.debug(msgCode + "当前消息代码配置 禁用了站内信");
                    return;
                }
                //替换模板内容
                String msgTempContent = msgInfoTpl.getTplMSG();
                msgTempContent = MsgTemplateUtil.replaceContent(msgTempContent, msgParam);
                msgSave(userId, msgInfo.getId(), sendTitle, sendType, sendTo, sendForm, receiveId, msgTempContent);
                break;
            case "EMAIL":
                //判断是否要发送邮件
                if (!msgInfo.isSupportEmail()) {
                    log.debug(msgCode + "当前消息代码配置 禁用了邮件");
                    return;
                }
                //替换模板内容
                String emailTempContent = msgInfoTpl.getTplEmail();
                emailTempContent = MsgTemplateUtil.replaceContent(emailTempContent, msgParam);
                if (isRecord) {
                    msgSave(userId, msgInfo.getId(), sendTitle, sendType, sendTo, sendForm, receiveId, emailTempContent);
                }
                emailService.send(receiveName, sendTitle, emailTempContent);
                break;
            case "SMS":
                //判断是否发送短信
                if (!msgInfo.isSupportSMS()) {
                    log.debug(msgCode + "当前消息代码配置 禁用了短信");
                    return;
                }
                //替换模板内容
                String smsTempContent = msgInfoTpl.getTplSMS();
                smsTempContent = MsgTemplateUtil.replaceContent(smsTempContent, msgParam);
                if (isRecord) {
                    msgSave(userId, msgInfo.getId(), sendTitle, sendType, sendTo, sendForm, receiveId, smsTempContent);
                }
                //发送短信
                smsService.send(msgInfo.getId(), receiveName, msgParam);
                break;
            default:
                log.debug("传入的消息类型为空,或者暂时不支持");

        }
    }

    /**
     * 发送消息保存
     */
    private void msgSave(String userId, String infoId, String title, String sendType, String sendTo, String sendForm, String receiveId, String tempContent) {
        Msg_send_msg msgSendMsg = new Msg_send_msg();
        msgSendMsg.setSendId(userId);
        msgSendMsg.setTitle(title);
        msgSendMsg.setType("system");
        msgSendMsg.setSendType(sendType);
        msgSendMsg.setSendTo(sendTo);
        msgSendMsg.setSendFrom(sendForm);
        msgSendMsg.setSendAll(false);
        Msg_receive_msg msgReceiveMsg = new Msg_receive_msg();
        msgReceiveMsg.setReceiveId(receiveId);//文君
        List<Msg_receive_msg> receiveMsgList = new ArrayList<Msg_receive_msg>();
        receiveMsgList.add(msgReceiveMsg);
        msgSendMsg.setReceiveMsgs(receiveMsgList);
        msgSendMsg.setNote(tempContent);
        //发送站内信
        msgSendMsgService.saveInfoMsg(msgSendMsg, infoId);
    }


}
