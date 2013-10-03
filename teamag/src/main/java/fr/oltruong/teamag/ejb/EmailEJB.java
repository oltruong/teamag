package fr.oltruong.teamag.ejb;

import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;

// from http://www.tutorialspoint.com/java/java_sending_email.htm
@Stateless
public class EmailEJB {

    @Inject
    private Logger logger;

    private static final String SENDER = "TEAMAG";

    @Inject
    private ParameterEJB parameterEJB;

    public void sendEmail(MailBean email) {

        // Sender's email ID needs to be mentioned

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", this.parameterEJB.getSmtpHost());

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = buildMessage(email, session);

            Transport.send(message);
            this.logger.debug("Message successfully sent");
        } catch (MessagingException messagingException) {
            this.logger.error("Error in sending message [" + messagingException.getMessage() + "]");
        }
    }

    private MimeMessage buildMessage(MailBean email, Session session) throws MessagingException, AddressException {
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(SENDER + "<" + this.parameterEJB.getAdministratorEmail() + ">"));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getRecipient()));

        message.setSubject(email.getSubject());

        message.setContent(email.getContent(), "text/plain");
        return message;
    }
}