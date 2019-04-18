package com.aebiz.app.msg.commons.sms;

import org.nutz.lang.util.NutMap;

/**
 * Created by wizzer on 2017/2/21.
 */
public interface SmsService {

    void send(String infoId, String mobile, NutMap map);

    void send(String smsId, String templateId, String mobile, NutMap map);
}
