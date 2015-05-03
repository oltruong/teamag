package com.oltruong.teamag.service;

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

// from http://www.tutorialspoint.com/java/java_sending_email.htm
@Stateless
public class EmailService extends AbstractService {


    private static final String SENDER = "TEAMAG";


    @Inject
    private ParameterService parameterService;


    public void sendEmailAdministrator(MailBean email) {
        email.setRecipient(parameterService.getAdministratorEmail());
        if (parameterService.getSmtpHost() != null) {
            sendEmail(email);
        } else {
            logger.warn("no email will be sent as SMTP HOST is not defined");
        }
    }


    private void sendEmail(MailBean email) {

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

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getRecipient()));

        message.setSubject(email.getSubject());

        message.setContent(email.getContent(), "text/plain");
        return message;
    }


    @Override
    Class entityProvider() {
        return null;
    }

    @Override
    public List findAll() {
        throw new UnsupportedOperationException();
    }
}