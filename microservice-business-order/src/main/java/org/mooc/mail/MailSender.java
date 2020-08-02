package org.mooc.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

public class MailSender {

    private final JavaMailSender sender;

    public MailSender(JavaMailSender sender){
        this.sender = sender;
    }

    public void sendText(String subject, String text){

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject(subject);
        mail.setText(text);

        sender.send(mail);

    }

    public void sendHtml(String subject, String html, String from, String... to) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);

        sender.send(message);
    }

    public void sendWithAttachment(String subject, String text, String[] fileNames) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject(subject);
        helper.setText(text);

        for (String fileName : fileNames){
            File file = new File(fileName);
            helper.addAttachment(fileName.substring(fileName.lastIndexOf(File.pathSeparator)), file);
        }

        sender.send(message);
    }

}
