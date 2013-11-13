package fr.oltruong.teamag.ejb;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.entity.Parameter;
import fr.oltruong.teamag.entity.ParameterName;
import fr.oltruong.teamag.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class ParameterEJBTest extends AbstractEJBTest {

    @Test
    public void testSaveAndReloadParameters() throws Exception {

        ParameterEJB parameterEJB = new ParameterEJB();
        TestUtils.setPrivateAttribute(parameterEJB, AbstractEJB.class, getMockEntityManager(), "entityManager");
        TestUtils.setPrivateAttribute(parameterEJB, getMockLogger(), "logger");
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

        verify(getMockEntityManager()).merge(eq(smtpParameter));
        verify(getMockEntityManager()).merge(eq(emailParameter));
        verify(getMockEntityManager(),times(2)).createNamedQuery(eq("findParameters"));
        verify(getMockQuery(),times(2)).getResultList();

    }
}
