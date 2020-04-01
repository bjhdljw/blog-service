package com.ljw.blogservice.service.mail;

import com.ljw.blogservice.domain.user.UserInfo;

import javax.mail.MessagingException;

public interface MailService {

    /**
     * 发送邮件
     * @param des
     * @param msg
     * @param title
     */
    void sendMail(String des, String msg, String title) throws MessagingException;

    void sendActiveMail(UserInfo userInfo, String activeCode) throws Exception;

}
