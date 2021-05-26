package org.jeecg.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;

/**
 * 发送邮件组件
 */
@Component
@Slf4j
public class EmailComponent {
    @Autowired
    private JavaMailSender javaMailSender;
    /**
     * 发件人邮箱
     */
    @Value("${spring.mail.username}")
    private String emailFrom;

    static {
        // 邮件发送携带附件,文件名称过长可能导致文件名被自动截取,添加如下语句表示不会自动截取文件名称
        System.getProperties().setProperty("mail.mime.splitlongparameters", "false");
    }

    /**
     * 发送简单邮件
     */
    public boolean sendSimpleMail(MailMessage mailMessage) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(emailFrom);
            msg.setTo(mailMessage.emailTo);
            msg.setSubject(mailMessage.subject);
            msg.setText(mailMessage.content);
            msg.setSentDate(new Date());
            javaMailSender.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("邮件发送失败:{}", e.getMessage());
            return false;
        }
    }

    /**
     * 发送携带附件的邮件
     */
    public boolean sendAttachFileMail(MailMessage mailMessage) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailFrom);
            helper.setTo(mailMessage.emailTo);
            helper.setSubject(mailMessage.subject);
            helper.setText(mailMessage.content);
            helper.setSentDate(new Date());
            for (String filePath : mailMessage.attachList) {
                File file = new File(filePath);
                helper.addAttachment(file.getName(), file);
            }
            javaMailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("邮件发送失败:{}", e.getMessage());
            return false;
        }
    }

    @Getter
    @Setter
    public static class MailMessage {
        /**
         * 收件人邮箱[可多人]
         */
        private String[] emailTo;
        /**
         * 邮件主题
         */
        private String subject;
        /**
         * 邮件内容
         */
        private String content;
        /**
         * 附件的路径[可添加多个附件发送]
         */
        private String[] attachList;
    }
}
