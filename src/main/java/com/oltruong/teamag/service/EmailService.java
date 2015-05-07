package com.oltruong.teamag.service;

import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@Stateless
public class EmailService {


    private static final String SENDER = "TEAMAG";

    @Inject
    protected Logger logger;

    @Inject
    private ParameterService parameterService;


    public void sendEmailAdministrator(MailBean email) {
        email.getRecipientList().clear();
        email.addRecipient(parameterService.getAdministratorEmail());

        sendEmail(email);
    }

    public void sendEmailCopyBlindAdministrator(MailBean email) {
        email.getBlindRecipientList().clear();
        email.addBlindRecipient(parameterService.getAdministratorEmail());

        sendEmail(email);
    }

    public void sendEmail(MailBean email) {
        if (parameterService.getSmtpHost() != null) {
            send(email);
        } else {
            logger.warn("no email will be sent as SMTP HOST is not defined");
        }
    }


    private void send(MailBean email) {

        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", parameterService.getSmtpHost());

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = buildMessage(email, session);
            Transport.send(message);
        } catch (MessagingException messagingException) {
            logger.error("Error in sending message [" + messagingException.getMessage() + "]");
        }

    }


    private MimeMessage buildMessage(MailBean email, Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(SENDER + "<" + parameterService.getAdministratorEmail() + ">"));

        addRecipient(message, Message.RecipientType.TO, email.getRecipientList());
        addRecipient(message, Message.RecipientType.BCC, email.getBlindRecipientList());

        message.setSubject(email.getSubject());

        message.setContent(email.getContent(), "text/plain");
        return message;
    }

    private void addRecipient(MimeMessage message, Message.RecipientType recipientType, List<String> recipientList) throws MessagingException {
        for (String recipient : recipientList) {
            message.addRecipient(recipientType, new InternetAddress(recipient));
        }
    }

}