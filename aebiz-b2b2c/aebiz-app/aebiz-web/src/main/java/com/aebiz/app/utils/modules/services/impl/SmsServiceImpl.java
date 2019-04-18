package com.aebiz.app.utils.modules.services.impl;


import com.aebiz.app.utils.modules.models.SmsQo;
import com.aebiz.app.utils.modules.services.SmsService;
import com.aebiz.app.web.commons.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 短信模块接口
 */
@Service
public class SmsServiceImpl implements SmsService {

    private static final Log log = Logs.get();

    @Autowired
    private PropertiesProxy config;


    @Override
    public boolean sendMessages(String msg,String mobile) {
        SmsQo smsQo = new SmsQo();
        String userName = config.get("sms.loginUserName");
        String userPwd = config.get("sms.userPwd");
        String srcAddr = config.get("sms.srcAddr");
        String exteCode = config.get("sms.exteCode");
        String url = config.get("sms.postUrl");
        smsQo.setClientType("11");
        smsQo.setUserName(userName);
        smsQo.setUserPwd(userPwd);
        smsQo.setSrcAddr(srcAddr);
        smsQo.setExteCode(exteCode);
        smsQo.setDestAddr(mobile);
        smsQo.setContent(msg);
        String dataStr = JSON.toJSONString(smsQo);
        log.info("发送短信请求数据："+dataStr);
        dataStr = HttpClientUtil.base64Utils(dataStr);
        try {
            JSONObject jsonObject = (JSONObject) HttpClientUtil.postMethod4(url,dataStr);
            Integer status=(Integer) jsonObject.get("status");
            if(status == null || status != 0){
               log.error("发送短信请求返回结果："+jsonObject.get("desc"));
               return false;
            }else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
