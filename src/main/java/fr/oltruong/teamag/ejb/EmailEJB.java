package fr.oltruong.teamag.ejb;

import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

// from http://www.tutorialspoint.com/java/java_sending_email.htm
@Stateless
public class EmailEJB {

    @Inject
    private Logger logger;

    private static final String SENDER = "TEAMAG";

    @Inject
    private ParameterEJB parameterEJB;

    public void sendEmailAdministrator(MailBean email) {

        email.setRecipient(parameterEJB.getAdministratorEmail());
        sendEmail(email);
    }

    private void sendEmail(MailBean email) {

        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", this.parameterEJB.getSmtpHost());

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = buildMessage(email, session);

            Transport.send(message);
            this.logger.debug("Message successfully sent");
        } catch (MessagingException messagingException) {
            this.logger.error("Error in sending message [" + messagingException.getMessage() + "]");
        }
    }

    private MimeMessage buildMessage(MailBean email, Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(SENDER + "<" + this.parameterEJB.getAdministratorEmail() + ">"));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getRecipient()));

        message.setSubject(email.getSubject());

        message.setContent(email.getContent(), "text/plain");
        return message;
    }
}