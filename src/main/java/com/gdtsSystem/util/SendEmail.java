package com.gdtsSystem.util;

import com.gdtsSystem.action.AdminServlet;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {

    public static void sendPasswd(String mail,String passwd)
    {
        Logger logger = Logger.getLogger(String.valueOf(AdminServlet.class));
        // 收件人电子邮箱
        String to = mail;

        // 发件人电子邮箱
        String from = "ixugeng@163.com";

        // 指定发送邮件的主机为 localhost
        String host = "smtp.163.com";

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("ixugeng@163.com", "NPBEEYSHKJLCNMBH"); //发件人邮件用户名、授权码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头部头字段
            message.setSubject("请妥善保管好您的密码!");

            // 设置消息体
            message.setText("Your password is:\t"+passwd);

            // 发送消息
            Transport.send(message);
            logger.debug("密码已发送至你的邮箱，请注意查收!");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public static void sendJiaoyan(String mail,int jiaoyan)
    {
        Logger logger = Logger.getLogger(String.valueOf(AdminServlet.class));
        // 收件人电子邮箱
        String to = mail;

        // 发件人电子邮箱
        String from = "ixugeng@163.com";

        // 指定发送邮件的主机为 localhost
        String host = "smtp.163.com";

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("ixugeng@163.com", "NPBEEYSHKJLCNMBH"); //发件人邮件用户名、授权码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头部头字段
            message.setSubject("[毕设选题系统]注册邮箱校验");

            // 设置消息体
            message.setText("Your verification code is:\t"+jiaoyan);

            // 发送消息
            Transport.send(message);
            logger.debug("校验码已发送至你的邮箱，请注意查收!");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}