package com.ljw.blogservice.service.mail.Impl;

import com.ljw.blogservice.domain.user.UserInfo;
import com.ljw.blogservice.init.MailInit;
import com.ljw.blogservice.service.mail.MailService;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {

    @Value("${mail.host}")
    private String host;
    @Value("${mail.username}")
    private String userName;
    @Value("${mail.code}")
    private String code;
    @Value("${mail.port}")
    private Integer port;

    @Async
    @Override
    public void sendMail(String des, String msg, String title) throws MessagingException {
        JavaMailSenderImpl mailSender = initMailSender();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "GBK");
        mimeMessageHelper.setFrom(userName);
        mimeMessageHelper.setTo(des);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(msg, true);
        //调用这个函数会阻塞一会
        mailSender.send(mimeMessage);

    }

    private JavaMailSenderImpl initMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(userName);
        //授权码
        mailSender.setPassword(code);
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.timeout", "25000");
        properties.setProperty("mail.smtp.auth", "false");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }

    @Override
    public void sendActiveMail(UserInfo userInfo, String activeCode) throws Exception{
        Map<String, String> parameters = new HashMap<>();
        parameters.put("activeCode", activeCode);
        String html = getHtml(parameters);
        sendMail(userInfo.getMail(), html, "激活邮件");
    }

    /**
     * 解析html，并且将参数写入html
     * @param parameters
     * @return
     * @throws Exception
     */
    public static String getHtml(Map<String, String> parameters) throws Exception{
        StringWriter stringWriter = new StringWriter();
        Template template = MailInit.getTemplate();
        template.process(parameters, stringWriter);
        return stringWriter.toString();
    }

}
