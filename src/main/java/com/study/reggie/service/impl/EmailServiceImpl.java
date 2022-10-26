package com.study.reggie.service.impl;

import com.study.reggie.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author That's all
 */
@Service
public class EmailServiceImpl implements EmailService {

    /**
     * 邮箱发送者
     */
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMail(String email, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        //设置发送邮件的账号
        message.setFrom(from);
        //设置接收邮件的账号
        message.setTo(email);
        //设置邮件主题
        message.setSubject(subject);
        //设置邮件内容
        message.setText(content);
        //执行发送
        mailSender.send(message);
    }
}
