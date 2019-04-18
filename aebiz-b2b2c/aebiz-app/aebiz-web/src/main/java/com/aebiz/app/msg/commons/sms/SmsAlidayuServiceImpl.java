package com.aebiz.app.msg.commons.sms;

import com.aebiz.app.msg.modules.models.Msg_conf_sms;
import com.aebiz.app.msg.modules.models.Msg_conf_sms_tpl;
import com.aebiz.app.msg.modules.services.MsgConfSmsService;
import com.aebiz.app.msg.modules.services.MsgConfSmsTplService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wizzer on 2017/2/21.
 */
@Service
public class SmsAlidayuServiceImpl implements SmsService {

    Log log = Logs.getLog(SmsAlidayuServiceImpl.class);

    @Autowired
    private MsgConfSmsService msgConfSmsService;

    @Autowired
    private MsgConfSmsTplService msgConfSmsTplService;


    /**
     *  调用阿里大于短信发送接口发送短信
     * @param infoId 消息代码
     * @param mobile 短信接收号码，触达时间要求高的建议单条发送（支持11位手机号单个或多个已英文逗号分隔，一次调用最多传200个号码，示例：18600000000,13911111111,13322222222）
     * @param map 阿里大于短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开
     */
    @SLog(description = "调用阿里大于短信接口发送短信")
    public void send(String infoId,String mobile, NutMap map) {
        //模板使用的是阿里大于的模板，不需要关联查询本系统模板信息
        Msg_conf_sms conf = msgConfSmsService.fetch(Cnd.where("disabled","=",false).and("delFlag","=",false));
        if(conf == null){
            log.debug("当前没有可启用的短信配置");
            return;
        }
        Msg_conf_sms_tpl msgConfSmsTpl = msgConfSmsTplService.fetch(Cnd.where("infoId","=",infoId).and("smsId","=",conf.getId()).and("delFlag","=",false));
        if(msgConfSmsTpl == null){
            log.debug("当前的消息代码没有配置短信模板");
            return;
        }
        send(conf, msgConfSmsTpl.getTplId(), mobile, map);
    }


    /**
     *  调用阿里大于短信发送接口发送短信
     *
     * @param smsId 短信配置ID
     * @param templateId 阿里大于短信模板ID
     * @param mobile 短信接收号码，触达时间要求高的建议单条发送（支持11位手机号单个或多个已英文逗号分隔，一次调用最多传200个号码，示例：18600000000,13911111111,13322222222）
     * @param map 阿里大于短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开
     */
    @SLog(description = "调用阿里大于短信接口发送短信")
    public void send(String smsId, String templateId, String mobile, NutMap map) {
        //模板使用的是阿里大于的模板，不需要关联查询本系统模板信息
        Msg_conf_sms conf = msgConfSmsService.fetch(smsId);
        send(conf, templateId, mobile, map);
    }

    /**
     *  调用阿里大于短信发送接口发送短信
     *
     * @param conf 短信配置对象
     * @param templateId 阿里大于短信模板ID
     * @param mobile 短信接收号码，触达时间要求高的建议单条发送（支持11位手机号单个或多个已英文逗号分隔，一次调用最多传200个号码，示例：18600000000,13911111111,13322222222）
     * @param map 阿里大于短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开
     */
    @SLog(description = "调用阿里大于短信接口发送短信")
    public void send(Msg_conf_sms conf, String templateId, String mobile, NutMap map) {
//        try {
//            String appkey = Strings.sNull(conf.getAppkey());
//            String secret = Strings.sNull(conf.getSecret());
//            String signname = Strings.sNull(conf.getSignname(), "阿里大于");
//            String url = Strings.sNull(conf.getUrl(), "http://gw.api.taobao.com/router/rest");
//            TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
//            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
//            req.setSmsType("normal");//短信类型，传入值请填写normal
//            req.setSmsFreeSignName(signname);
//            req.setSmsParamString(Json.toJson(map));
//            req.setRecNum(mobile);
//            req.setSmsTemplateCode(templateId);
//            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
//            log.debug("发送信息返回结果: "+rsp);
//            //rsp.getBody();
//            //TODO 返回值怎么处理
//        } catch (ApiException e) {
//            log.debug(e.getMessage(),e);
//            //TODO 发送失败了怎么处理
//        }
    }

}
