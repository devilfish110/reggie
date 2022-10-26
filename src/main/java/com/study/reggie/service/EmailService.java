package com.study.reggie.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author That's all
 */
@Transactional(rollbackFor = Exception.class)
public interface EmailService {
    /**
     * 发送邮件
     *
     * @param email   收件人邮箱地址
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public abstract void sendMail(String email, String subject, String content);
}
