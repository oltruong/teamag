package com.oltruong.teamag.rest;

import com.oltruong.teamag.interfaces.AdminChecked;
import com.oltruong.teamag.model.Parameter;
import com.oltruong.teamag.service.AbstractService;
import com.oltruong.teamag.service.ParameterService;

import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Path("parameter")
@Stateless
@AdminChecked
public class ParameterEndPoint extends AbstractEndPoint<Parameter> {
    @Inject
    ParameterService parameterService;

    private int smtpPort = 2500;

    @Override
    AbstractService<Parameter> getService() {
        return parameterService;
    }


    @PUT
    public Response update(List<Parameter> parameterList) {
        parameterList.forEach(parameterService::merge);
        return ok();
    }

    @GET
    @Path("/test")
    public Response testEmail() {


        // Sender's email ID needs to be mentioned

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", parameterService.getSmtpHost());
        properties.setProperty("mail.smtp.port", String.valueOf(smtpPort));

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("TEAMAG<" + parameterService.getAdministratorEmail() + ">"));

            // Set To: header field of the header.

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(parameterService.getAdministratorEmail()));

            // Set Subject: header field
            message.setSubject("Test");

            // Send the actual HTML message, as big as you like
            message.setContent("Ceci est un test.", "text/plain");


            // Send message
            Transport.send(message);


        } catch (MessagingException messagingException) {
            return internalError();
        }
        return ok();

    }
}
