package fr.oltruong.teamag.backingbean;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.ejb.AbsenceEJB;
import fr.oltruong.teamag.ejb.EmailEJB;
import fr.oltruong.teamag.ejb.WorkLoadEJB;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.EntityFactory;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.utils.MessageManager;
import fr.oltruong.teamag.utils.TestUtils;
import fr.oltruong.teamag.webbean.AbsenceWebBean;
import fr.oltruong.teamag.webbean.WebBeanFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import javax.enterprise.inject.Instance;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class AbsenceControllerTest extends ControllerTest {

    @Mock
    private AbsenceEJB mockAbsenceEJB;

    @Mock
    private WorkLoadEJB mockWorkLoadEJB;

    @Mock
    private EmailEJB mockEmailEJB;

    @Mock
    private Instance<Member> mockMemberInstance;

    @Mock
    private Member mockMember;

    private AbsenceController absenceController;


    @Before
    public void setup() {
        super.setup();
        absenceController = new AbsenceController();
        when(mockMemberInstance.get()).thenReturn(mockMember);
        TestUtils.setPrivateAttribute(absenceController, mockAbsenceEJB, "absenceEJB");
        TestUtils.setPrivateAttribute(absenceController, mockWorkLoadEJB, "workLoadEJB");
        TestUtils.setPrivateAttribute(absenceController, mockEmailEJB, "emailEJB");
        TestUtils.setPrivateAttribute(absenceController, mockMemberInstance, "member");
        TestUtils.setPrivateAttribute(absenceController, Controller.class, mockMessageManager, "messageManager");

    }

    @Test
    public void testInit() throws Exception {

        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence);
        when(mockAbsenceEJB.findAbsencesByMember(eq(mockMember))).thenReturn(absenceList);
        String view = absenceController.init();

        assertThat(view).isEqualTo(TestUtils.getPrivateAttribute(absenceController, "VIEWNAME"));
        verify(mockAbsenceEJB).findAbsencesByMember(eq(mockMember));

        assertThat(absenceController.getAbsencesList()).hasSameSizeAs(absenceList);

    }

    @Test
    public void testAddAbsence() throws Exception {
        AbsenceWebBean absenceWebBean = WebBeanFactory.generateAbsenceWebBean();
        absenceController.setAbsence(absenceWebBean);

        absenceController.addAbsence();

        ArgumentCaptor<Absence> argument = ArgumentCaptor.forClass(Absence.class);
        verify(mockAbsenceEJB).addAbsence(argument.capture());
        assertThat(argument.getValue().getBeginDate().withTimeAtStartOfDay()).isEqualTo(absenceWebBean.getBeginDateTime().withTimeAtStartOfDay());
        assertThat(argument.getValue().getEndDate().withTimeAtStartOfDay()).isEqualTo(absenceWebBean.getEndDateTime().withTimeAtStartOfDay());
        assertThat(argument.getValue().getBeginType().intValue()).isEqualTo(absenceWebBean.getBeginType());
        assertThat(argument.getValue().getEndType().intValue()).isEqualTo(absenceWebBean.getEndType());
        verify(mockAbsenceEJB).findAbsencesByMember(eq(mockMember));
        verify(mockMessageManager).displayMessage(eq(MessageManager.INFORMATION), anyString());


        //Check absence has been reset
        assertThat(absenceController.getAbsence().getBeginDate()).isNull();
        assertThat(absenceController.getAbsence().getEndDate()).isNull();
        assertThat(absenceController.getAbsence().getBeginType()).isEqualTo(Absence.ALL_DAY);
        assertThat(absenceController.getAbsence().getEndType()).isEqualTo(Absence.ALL_DAY);

    }


    @Test
    public void testAddAbsence_formatBeginDate() throws Exception {
        AbsenceWebBean absenceWebBean = WebBeanFactory.generateAbsenceWebBean();
        absenceWebBean.setBeginDate(null);
        absenceWebBean.setBeginType(0);
        absenceController.setAbsence(absenceWebBean);

        absenceController.addAbsence();
        assertThat(absenceWebBean.getBeginDate()).isEqualTo(absenceWebBean.getEndDate());
        assertThat(absenceWebBean.getBeginType()).isEqualTo(absenceWebBean.getEndType());
    }

    @Test
    public void testAddAbsence_formatEndDate() throws Exception {
        AbsenceWebBean absenceWebBean = WebBeanFactory.generateAbsenceWebBean();
        absenceWebBean.setEndDate(null);
        absenceWebBean.setEndType(0);
        absenceController.setAbsence(absenceWebBean);

        absenceController.addAbsence();
        assertThat(absenceWebBean.getEndDate()).isEqualTo(absenceWebBean.getBeginDate());
        assertThat(absenceWebBean.getEndType()).isEqualTo(absenceWebBean.getBeginType());
    }


    @Test
    public void testAddAbsence_inconsistent() throws Exception {
        AbsenceWebBean absenceWebBean = WebBeanFactory.generateAbsenceWebBean();

        absenceWebBean.setBeginDateTime(absenceWebBean.getEndDateTime().plusDays(4));
        absenceController.setAbsence(absenceWebBean);

        absenceController.addAbsence();

        ArgumentCaptor<Absence> argument = ArgumentCaptor.forClass(Absence.class);
        verify(mockAbsenceEJB, never()).addAbsence(isA(Absence.class));
        verify(mockAbsenceEJB, never()).findAbsencesByMember(eq(mockMember));
        verify(mockMessageManager).displayMessageWithDescription(eq(MessageManager.ERROR), anyString(), anyString());
    }

    @Test
    public void testAddAbsence_overlap() throws Exception {
        AbsenceWebBean absenceWebBean = WebBeanFactory.generateAbsenceWebBean();

        absenceController.setAbsence(absenceWebBean);

        List<AbsenceWebBean> absenceWebBeanList = Lists.newArrayListWithExpectedSize(1);
        absenceWebBeanList.add(absenceWebBean);
        absenceController.setAbsencesList(absenceWebBeanList);

        absenceController.addAbsence();

        ArgumentCaptor<Absence> argument = ArgumentCaptor.forClass(Absence.class);
        verify(mockAbsenceEJB, never()).addAbsence(isA(Absence.class));
        verify(mockAbsenceEJB, never()).findAbsencesByMember(eq(mockMember));
        verify(mockMessageManager).displayMessageWithDescription(eq(MessageManager.ERROR), anyString(), anyString());
    }

    @Test
    public void testDeleteAbsence() throws Exception {
        long absenceId = 312l;
        AbsenceWebBean absenceWebBean = WebBeanFactory.generateAbsenceWebBean();
        absenceWebBean.setId(Long.valueOf(absenceId));

        absenceController.setSelectedAbsence(absenceWebBean);
        absenceController.deleteAbsence();

        verify(mockAbsenceEJB).deleteAbsence(eq(absenceWebBean.getId()));
        verify(mockAbsenceEJB).findAbsencesByMember(eq(mockMember));
    }
}
