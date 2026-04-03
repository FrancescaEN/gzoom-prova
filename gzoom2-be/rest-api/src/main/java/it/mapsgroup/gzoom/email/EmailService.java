package it.mapsgroup.gzoom.email;

import it.mapsgroup.gzoom.email.configuration.EmailConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

@Service
public class EmailService implements EmailServiceInterface {
    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender emailSender;

    private final SimpleMailMessage template;

    private final String mailFrom;

    private final String mailRedirectTo;


    @Autowired
    public EmailService(JavaMailSender emailSender, SimpleMailMessage template, EmailConfiguration emailConfiguration) {
        this.emailSender = emailSender;
        this.template = template;
        this.mailFrom = emailConfiguration.getMailFrom();
        this.mailRedirectTo = emailConfiguration.getMailRedirectTo();
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(this.mailFrom);

            message.setTo((this.mailRedirectTo != null)? this.mailRedirectTo: to);
            message.setSubject(subject);
            message.setText(text);

            this.emailSender.send(message);
        } catch (MailException exception) {
            LOG.error("Error send SimpleMessage, MailException: ", exception);
        }
    }
    @Override
    public void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(this.mailFrom);
        helper.setTo((this.mailRedirectTo != null)? this.mailRedirectTo: to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        emailSender.send(message);
    }

    @Override
    public void sendSimpleMessageUsingTemplate(String to,
                                               String subject,
                                               String ...templateModel) {
        String text = String.format(Objects.requireNonNull(template.getText()), (Object) templateModel);
        sendSimpleMessage(to, subject, text);
    }

    @Override
    public void sendMessageWithAttachment(String to,
                                          String subject,
                                          String text,
                                          String pathToAttachment) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            // pass 'true' to the constructor to create a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(this.mailFrom);
            helper.setTo((this.mailRedirectTo != null)? this.mailRedirectTo: to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            emailSender.send(message);
        } catch (MessagingException e) {
            LOG.error("Error sending simple message: ", e);
        }
    }

}