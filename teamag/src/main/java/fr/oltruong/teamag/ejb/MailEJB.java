package fr.oltruong.teamag.ejb;

import java.util.ArrayList;
import java.util.List;
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

    // FIXME
    private String smtphost = "TODEFINE";

    public void sendEmail( String from, String recipient, String subject, String content )
    {
        List<String> recipientsList = new ArrayList<String>( 1 );
        recipientsList.add( recipient );

        sendEmail( from, recipientsList, null, null, subject, content );

    }

    public void sendEmail( String from, List<String> recipients, List<String> recipientsCC, List<String> recipientsBCC,
                           String subject, String content )
    {

        // Sender's email ID needs to be mentioned

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty( "mail.smtp.host", smtphost );

        // Get the default Session object.
        Session session = Session.getDefaultInstance( properties );

        try
        {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage( session );

            // Set From: header field of the header.
            message.setFrom( new InternetAddress( from ) );

            // Set To: header field of the header.

            if ( recipients != null )
            {
                for ( String recipient : recipients )
                {
                    message.addRecipient( Message.RecipientType.TO, new InternetAddress( recipient ) );
                }

            }

            if ( recipientsCC != null )
            {
                for ( String recipient : recipientsCC )
                {
                    message.addRecipient( Message.RecipientType.CC, new InternetAddress( recipient ) );
                }

            }

            if ( recipientsBCC != null )
            {
                for ( String recipient : recipientsBCC )
                {
                    message.addRecipient( Message.RecipientType.BCC, new InternetAddress( recipient ) );
                }

            }

            // Set Subject: header field
            message.setSubject( subject );

            // Send the actual HTML message, as big as you like
            message.setContent( content, "text/plain" );

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