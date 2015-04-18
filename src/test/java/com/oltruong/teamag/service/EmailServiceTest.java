package com.oltruong.teamag.service;

import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmailServiceTest extends AbstractServiceTest {


    @Mock
    ParameterService mockParameterService;


    private EmailService emailService;
    private MailBean email;

    @Before
    public void prepare() {
        super.setup();
        email = new MailBean();

        emailService = new EmailService();
        prepareService(emailService);
        TestUtils.setPrivateAttribute(emailService, mockParameterService, "parameterService");
    }


    @Test
    public void testSendEmail_noSMTP() throws Exception {
        emailService.sendEmailAdministrator(email);
        verify(getMockLogger()).warn(anyString());
    }

    @Test
    public void testSendEmailAdministrator() throws Exception {
        email.setContent("content");
        email.setSubject("subject");

        when(mockParameterService.getSmtpHost()).thenReturn("");
        when(mockParameterService.getAdministratorEmail()).thenReturn("email@email.com");
        emailService.sendEmailAdministrator(email);
        verify(getMockLogger()).error(anyString());
    }
}