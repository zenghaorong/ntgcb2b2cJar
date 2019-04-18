package com.aebiz.app.utils.modules.services;


/***
 * 短信模块接口
 */
public interface SmsService{


    /**
     * 发送短信
     */
    boolean sendMessages(String msg,String mobile);


}
