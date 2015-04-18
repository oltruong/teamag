package com.oltruong.teamag.service;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.enumeration.ParameterName;
import com.oltruong.teamag.model.Parameter;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Olivier Truong
 */
public class ParameterServiceTest extends AbstractServiceTest {

    @Test
    public void testSaveAndReloadParameters() throws Exception {

        ParameterService parameterEJB = new ParameterService();
        prepareService(parameterEJB);
        TestUtils.callPrivateMethod(parameterEJB, "initValue");

        Parameter smtpParameter = new Parameter();
        smtpParameter.setName(ParameterName.SMTP_HOST);
        smtpParameter.setValue("smtp.test.com");

        Parameter emailParameter = new Parameter();
        emailParameter.setName(ParameterName.ADMINISTRATOR_EMAIL);
        emailParameter.setValue("admin@test.com");


        List<Parameter> parameterList = Lists.newArrayListWithExpectedSize(2);
        parameterList.add(emailParameter);
        parameterList.add(smtpParameter);

        when(getMockQuery().getResultList()).thenReturn(parameterList);

        parameterEJB.saveAndReloadParameters(smtpParameter, emailParameter);

        assertThat(parameterEJB.getAdministratorEmail()).isEqualTo(emailParameter.getValue());
        assertThat(parameterEJB.getSmtpHost()).isEqualTo(smtpParameter.getValue());

        verify(mockEntityManager).merge(eq(smtpParameter));
        verify(mockEntityManager).merge(eq(emailParameter));
        verify(mockEntityManager, times(2)).createNamedQuery(eq("findParameters"));
        verify(getMockQuery(), times(2)).getResultList();

    }
}
