package fr.oltruong.teamag.ejb;

import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// from http://www.tutorialspoint.com/java/java_sending_email.htm
@Stateless
public class MailEJB
{
    public void main()
    {

        // Recipient's email ID needs to be mentioned.
        String to = "";

        // Sender's email ID needs to be mentioned
        String from = "TEAMAG <web@gmail.com>";

        // Assuming you are sending email from localhost
        String host = "";// smtp

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty( "mail.smtp.host", host );

        // Get the default Session object.
        Session session = Session.getDefaultInstance( properties );

        try
        {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage( session );

            // Set From: header field of the header.
            message.setFrom( new InternetAddress( from ) );

            // Set To: header field of the header.
            message.addRecipient( Message.RecipientType.TO, new InternetAddress( to ) );

            message.addRecipient( Message.RecipientType.BCC, new InternetAddress( "" ) );

            // Set Subject: header field
            message.setSubject( "This is the Subject Line!" );

            // Send the actual HTML message, as big as you like
            message.setContent( "<h1>This is actual message</h1>", "text/html" );

            // Send message
            Transport.send( message );
            System.out.println( "Sent message successfully...." );
        }
        catch ( MessagingException mex )
        {
            mex.printStackTrace();
        }
    }
}