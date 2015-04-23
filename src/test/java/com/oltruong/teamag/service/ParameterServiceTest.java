package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Parameter;
import com.oltruong.teamag.model.enumeration.ParameterName;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class ParameterServiceTest extends AbstractServiceTest {

    @Test
    public void testSaveAndReloadParameters() throws Exception {

        ParameterService parameterService = new ParameterService();
        prepareService(parameterService);


        TestUtils.callPrivateMethod(parameterService, "initValue");

        Parameter smtpParameter = new Parameter();
        smtpParameter.setName(ParameterName.SMTP_HOST);
        smtpParameter.setValue("smtp.test.com");

        Parameter emailParameter = new Parameter();
        emailParameter.setName(ParameterName.ADMINISTRATOR_EMAIL);
        emailParameter.setValue("admin@test.com");


        List<Parameter> parameterList = Lists.newArrayListWithExpectedSize(2);
        parameterList.add(emailParameter);
        parameterList.add(smtpParameter);

        when(mockTypedQuery.getResultList()).thenReturn(parameterList);

        parameterService.saveAndReloadParameters(smtpParameter, emailParameter);

        assertThat(parameterService.getAdministratorEmail()).isEqualTo(emailParameter.getValue());
        assertThat(parameterService.getSmtpHost()).isEqualTo(smtpParameter.getValue());

        verify(mockEntityManager).merge(eq(smtpParameter));
        verify(mockEntityManager).merge(eq(emailParameter));
        verify(mockEntityManager, times(2)).createNamedQuery(eq("findParameters"), eq(Parameter.class));
        verify(mockTypedQuery, times(2)).getResultList();

    }
}
