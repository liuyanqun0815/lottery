package com.cj.lottery.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class MailUtils {

    private String lyqQQ = "1163866926@qq.com";

    private String zhQQ = "zhangshuo@keyundz.cn";

    private String sendQq = "1163866926@qq.com";
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String ex) {
//      第一种
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(sendQq);//发送者
        msg.setTo(lyqQQ);//接收者
        msg.setSubject("异常报警");//标题
        msg.setText(ex);//内容
        javaMailSender.send(msg);

        SimpleMailMessage msg2 = new SimpleMailMessage();
        msg2.setFrom(sendQq);//发送者
        msg2.setTo(zhQQ);//接收者
        msg2.setSubject("异常报警");//标题
        msg2.setText(ex);//内容
        javaMailSender.send(msg);
    }
}
