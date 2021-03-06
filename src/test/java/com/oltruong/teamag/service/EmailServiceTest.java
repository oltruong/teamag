package com.oltruong.teamag.service;

import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {


    @Mock
    ParameterService mockParameterService;

    @Mock
    Logger mockLogger;


    private EmailService emailService;
    private MailBean email;
    private String adminEmail = "email@email.com";

    @Before
    public void prepare() {
        email = new MailBean();
        email.addRecipient("foo@bar.com");
        email.setContent("content");
        email.setSubject("subject");

        emailService = new EmailService();

        TestUtils.setPrivateAttribute(emailService, mockParameterService, "parameterService");
        TestUtils.setPrivateAttribute(emailService, mockLogger, "logger");
    }


    @Test
    public void testSendEmail_noSMTP() throws Exception {
        emailService.sendEmailAdministrator(email);
        verify(mockLogger).warn(anyString());
    }

    @Test
    public void testSendEmailAdministrator() throws Exception {


        when(mockParameterService.getSmtpHost()).thenReturn("");
        when(mockParameterService.getSmtpPort()).thenReturn("9999");
        when(mockParameterService.getAdministratorEmail()).thenReturn(adminEmail);
        emailService.sendEmailAdministrator(email);
        verify(mockLogger).error(anyString());

        assertThat(email.getRecipientList()).containsExactly(adminEmail);
        assertThat(email.getBlindRecipientList()).isEmpty();
    }

    @Test
    public void testSendEmailCopyBlindAdministrator() throws Exception {

        when(mockParameterService.getSmtpHost()).thenReturn("");
        when(mockParameterService.getSmtpPort()).thenReturn("9999");
        when(mockParameterService.getAdministratorEmail()).thenReturn(adminEmail);

        emailService.sendEmailCopyBlindAdministrator(email);
        verify(mockLogger).error(anyString());

        assertThat(email.getRecipientList()).containsExactly("foo@bar.com");
        assertThat(email.getBlindRecipientList()).containsExactly(adminEmail);
    }
}