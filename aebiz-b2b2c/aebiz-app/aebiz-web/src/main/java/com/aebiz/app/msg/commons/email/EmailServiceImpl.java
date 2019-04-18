package com.aebiz.app.msg.commons.email;

import com.aebiz.app.msg.modules.models.Msg_conf_email;
import com.aebiz.app.msg.modules.services.MsgConfEmailService;
import com.aebiz.commons.utils.SpringUtil;
import org.apache.commons.mail.HtmlEmail;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wizzer on 2017/2/21.
 */
@Service
public class EmailServiceImpl implements EmailService {
    private Log log = Logs.get();
    @Autowired
    private MsgConfEmailService msgConfEmailService;
    public boolean send(String to, String subject, String html) {
        try {
            Msg_conf_email confEmail=msgConfEmailService.getConfEmail();
            HtmlEmail email = new HtmlEmail();
            email.setHostName(confEmail.getSmtpUrl());
            email.setSmtpPort(confEmail.getSmtpPort());
            email.setAuthentication(confEmail.getSmtpUsername(),confEmail.getSmtpPassword());
            email.setFrom(confEmail.getSendEmail());
            email.setSSLOnConnect(true);
            email.setCharset("utf-8");
            email.setSubject(subject);
            email.setHtmlMsg(html);
            email.addTo(to);
            email.buildMimeMessage();
            email.sendMimeMessage();
            return true;
        } catch (Throwable e) {
            log.info("send email fail", e);
            return false;
        }
    }
}
