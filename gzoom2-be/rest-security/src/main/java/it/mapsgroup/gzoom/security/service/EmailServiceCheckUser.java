package it.mapsgroup.gzoom.security.service;

import it.mapsgroup.gzoom.email.EmailService;
import it.mapsgroup.gzoom.security.JwtLogoutFilter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class EmailServiceCheckUser {
    private static final Logger LOG = getLogger(JwtLogoutFilter.class);
    @Autowired
    private EmailService emailService;


    public void sendHtmlMessage(String to, String subject, String text) {
        try {
            this.emailService.sendHtmlMessage(to, subject, text);
        } catch (MailException | MessagingException exception) {
            LOG.error("Error send SimpleMessage, MailException: {}", exception);
        }
    }
}