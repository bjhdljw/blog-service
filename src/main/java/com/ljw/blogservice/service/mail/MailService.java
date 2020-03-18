package com.ljw.blogservice.service.mail;

import javax.mail.MessagingException;

public interface MailService {

    /**
     * 发送邮件
     * @param des
     * @param msg
     * @param title
     */
    void sendMail(String des, String msg, String title) throws MessagingException;

}
