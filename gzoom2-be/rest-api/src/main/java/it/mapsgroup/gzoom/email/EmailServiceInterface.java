package it.mapsgroup.gzoom.email;


import jakarta.mail.MessagingException;

public interface EmailServiceInterface {
    void sendSimpleMessage(String to,
                           String subject,
                           String text);

    void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException;

    void sendSimpleMessageUsingTemplate(String to,
                                        String subject,
                                        String ...templateModel);
    void sendMessageWithAttachment(String to,
                                   String subject,
                                   String text,
                                   String pathToAttachment);
}