package fr.oltruong.teamag.controller;

import java.util.Properties;
import java.util.logging.Logger;

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

import fr.oltruong.teamag.ejb.EmailEJB;
import fr.oltruong.teamag.ejb.ParameterEJB;
import fr.oltruong.teamag.entity.Parameter;

@ManagedBean
@ApplicationScoped
public class ParameterController
{

    private final Logger logger = Logger.getLogger( getClass().getName() );

    // ======================================
    // = Attributes =
    // ======================================
    @EJB
    private ParameterEJB parametersEJB;

    @EJB
    private EmailEJB emailEJB;

    private Parameter smtpHostParameter;

    private Parameter administratorEmailParameter;

    @PostConstruct
    private void initList()
    {
        this.smtpHostParameter = this.parametersEJB.getSmtpHostParameter();
        this.administratorEmailParameter = this.parametersEJB.getAdministratorEmailParameter();
    }

    public String doUpdateParameters()
    {
        this.parametersEJB.setSmtpHostParameter( this.smtpHostParameter );
        this.parametersEJB.setAdministratorEmailParameter( this.administratorEmailParameter );
        this.parametersEJB.saveAndReloadParameters();

        initList();

        FacesMessage msg = null;
        msg = new FacesMessage( FacesMessage.SEVERITY_INFO, "Mise à jour effectuée", "Paramètres mis à jour" );
        FacesContext.getCurrentInstance().addMessage( null, msg );

        return "parameters.xhtml";
    }

    public void testEmail()
    {

        this.logger.info( "Sending test email" );

        // Sender's email ID needs to be mentioned

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty( "mail.smtp.host", this.smtpHostParameter.getValue() );

        // Get the default Session object.
        Session session = Session.getDefaultInstance( properties );

        try
        {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage( session );

            // Set From: header field of the header.
            message.setFrom( new InternetAddress( "TEAMAG<" + this.administratorEmailParameter.getValue() + ">" ) );

            // Set To: header field of the header.

            message.addRecipient( Message.RecipientType.TO,
                                  new InternetAddress( this.administratorEmailParameter.getValue() ) );
            this.logger.info( "Message to: " + this.administratorEmailParameter.getValue() );

            // Set Subject: header field
            message.setSubject( "Test" );

            // Send the actual HTML message, as big as you like
            message.setContent( "this is a test. Please ignore", "text/plain" );

            this.logger.info( "Sending message" );

            // Send message
            Transport.send( message );

            this.logger.info( "Message sent" );

            FacesMessage msg = null;
            msg =
                new FacesMessage( FacesMessage.SEVERITY_INFO, "Message envoyé", "Envoyé à "
                    + this.administratorEmailParameter.getValue() );
            FacesContext.getCurrentInstance().addMessage( null, msg );
        }
        catch ( MessagingException messagingException )
        {
            this.logger.severe( "Error when sending message [" + messagingException.getMessage() + "]" );
        }

    }

    public String getSmtpHostParameterValue()
    {
        return this.smtpHostParameter.getValue();
    }

    public String getAdministratorEmailParameterValue()
    {
        return this.administratorEmailParameter.getValue();
    }

    public void setSmtpHostParameterValue( String value )
    {
        this.smtpHostParameter.setValue( value );
    }

    public void setAdministratorEmailParameterValue( String value )
    {
        this.administratorEmailParameter.setValue( value );
    }
}
