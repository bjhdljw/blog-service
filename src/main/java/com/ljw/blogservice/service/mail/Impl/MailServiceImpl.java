package com.ljw.blogservice.service.mail.Impl;

import com.ljw.blogservice.service.mail.MailService;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {

    @Async
    @Override
    public void sendMail(String des, String msg, String title) throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.163.com");
        mailSender.setPort(25);
        mailSender.setUsername("lnfsljw@163.com");
        //授权码
        mailSender.setPassword("");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.timeout", "25000");
        properties.setProperty("mail.smtp.auth", "false");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailSender.setJavaMailProperties(properties);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "GBK");
        mimeMessageHelper.setFrom("lnfsljw@163.com");
        mimeMessageHelper.setTo(des);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(msg);
        //调用这个函数会阻塞一会
        mailSender.send(mimeMessage);

    }
}
