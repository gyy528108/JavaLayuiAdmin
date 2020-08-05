package com.lowi.admin.utils;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.lowi.admin.pojo.dto.EmailDto;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * sendMailUtils.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/5/20 16:22
 */
public class SendMailUtils {

    static String HOST = "smtp.163.com"; // smtp服务器
    static String token = "NNBJFVBFXKFECDUF";
    public static String myEmail = "hnpclowi@163.com";
    public static int ALIDM_SMTP_PORT = 25;


    public static void main(String[] args) {
        String numbers = RandomUtil.randomNumbers(6);
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE>" + "<div bgcolor='#f1fcfa'  style='border:1px solid #d9f4ee; font-size:14px; line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;padding-bottom:5px;text-align:center;'>"
                + "<div style='width:950px;font-family:arial;'>欢迎使用Lowi后台管理服务，您的注册码为：<br/><h2 style='color:green'>" + numbers + "</h2><br/>本邮件由系统自动发出，请勿回复。<br/>感谢您的使用。<br/>祝您生活愉快。</div>"
                + "</div>");
        boolean sendMail = SendMailUtils.sendMail("lowi_saisai@qq.com", "Lowi注册验证", sb.toString());
    }


    public static boolean sendMail(String to, String subject, String content) {
        if (to != null) {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", HOST);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", ALIDM_SMTP_PORT);
            MailAuthenticator auth = new MailAuthenticator();
            MailAuthenticator.USERNAME = myEmail;
            MailAuthenticator.PASSWORD = token;
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(myEmail));
                if (!"".equals(to.trim())) {
                    message.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(to.trim()));
                }
                message.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(myEmail));
                message.setSubject(subject);
                MimeBodyPart mbp1 = new MimeBodyPart(); // 正文
                mbp1.setContent(content, "text/html;charset=utf-8");
                Multipart mp = new MimeMultipart(); // 整个邮件：正文+附件
                mp.addBodyPart(mbp1);
                // mp.addBodyPart(mbp2);
                message.setContent(mp);
                message.setSentDate(new Date());
                message.saveChanges();
                session.getTransport("smtp");
                Transport.send(message);
                System.out.println(message.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
