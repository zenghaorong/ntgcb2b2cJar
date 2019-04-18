package com.aebiz.app.msg.commons.email;

/**
 * Created by wizzer on 2017/2/22.
 */
public interface EmailService {
    boolean send(String to, String subject, String html);
}
