package fr.oltruong.teamag.controller;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import fr.oltruong.teamag.ejb.ApplicationParametersEJB;
import fr.oltruong.teamag.entity.ApplicationParameters;

@ManagedBean
@ApplicationScoped
public class ParametersController
{

    // ======================================
    // = Attributes =
    // ======================================
    @EJB
    private ApplicationParametersEJB parametersEJB;

    private ApplicationParameters parameters;

    @PostConstruct
    private void initList()
    {
        parameters = parametersEJB.getParameters();
    }

    public String doUpdateParameters()
    {
        parametersEJB.setParameters( parameters );
        parametersEJB.saveParameters();

        parameters = parametersEJB.getParameters();

        FacesMessage msg = null;
        msg = new FacesMessage( FacesMessage.SEVERITY_INFO, "Mise à jour effectuée", "Paramètres mis à jour" );
        FacesContext.getCurrentInstance().addMessage( null, msg );

        return "parameters.xhtml";
    }

    public void testEmail()
    {

        // Sender's email ID needs to be mentioned

        // Get system properties
        Properties properties = System.getProperties();

        System.out.println( "SMTP" + parametersEJB.getParameters().getSmtpHost() );
        // Setup mail server
        properties.setProperty( "mail.smtp.host", parametersEJB.getParameters().getSmtpHost() );

        // Get the default Session object.
        Session session = Session.getDefaultInstance( properties );

        try
        {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage( session );

            // Set From: header field of the header.
            message.setFrom( new InternetAddress( parametersEJB.getParameters().getAdministratorEmail() ) );

            // Set To: header field of the header.

            message.addRecipient( Message.RecipientType.TO,
                                  new InternetAddress( parametersEJB.getParameters().getAdministratorEmail() ) );
            System.out.println( "TOOO" + parametersEJB.getParameters().getAdministratorEmail() );

            // Set Subject: header field
            message.setSubject( "Test" );

            // Send the actual HTML message, as big as you like
            message.setContent( "this is a test. Please ignore", "text/plain" );

            // Send message
            Transport.send( message );
        }
        catch ( MessagingException mex )
        {
            mex.printStackTrace();
        }
        System.out.println( "Sent message successfully...." );

    }

    public ApplicationParameters getParameters()
    {
        return parameters;
    }

}
