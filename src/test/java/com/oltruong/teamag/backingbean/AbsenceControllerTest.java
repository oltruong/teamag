package com.oltruong.teamag.backingbean;

import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.AbsenceService;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.webbean.WebBeanFactory;
import com.oltruong.teamag.exception.InconsistentDateException;
import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.utils.MessageManager;
import com.oltruong.teamag.webbean.AbsenceWebBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.enterprise.inject.Instance;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class AbsenceControllerTest extends ControllerTest {

    @Mock
    private AbsenceService mockAbsenceService;


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
        TestUtils.setPrivateAttribute(absenceController, mockAbsenceService, "absenceService");
        TestUtils.setPrivateAttribute(absenceController, mockMemberInstance, "member");
        TestUtils.setPrivateAttribute(absenceController, Controller.class, mockMessageManager, "messageManager");

    }

    @Test
    public void testInit() throws Exception {

        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence);
        when(mockAbsenceService.findAbsencesByMember(eq(mockMember.getId()))).thenReturn(absenceList);
        String view = absenceController.init();

        assertThat(view).isEqualTo(TestUtils.getPrivateAttribute(absenceController, "VIEWNAME"));
        verify(mockAbsenceService).findAbsencesByMember(eq(mockMember.getId()));

        assertThat(absenceController.getAbsencesList()).hasSameSizeAs(absenceList);

    }


    @Test
    public void testAddAbsence_inconsistent() throws Exception {
        AbsenceWebBean absenceWebBean = WebBeanFactory.generateAbsenceWebBean();

        absenceWebBean.setBeginDateTime(absenceWebBean.getEndDateTime().plusDays(4));
        absenceController.setAbsence(absenceWebBean);


        doThrow(new InconsistentDateException()).when(mockAbsenceService).addAbsence(isA(Absence.class), anyLong());

        absenceController.addAbsence();

        verify(mockAbsenceService).addAbsence(isA(Absence.class), anyLong());
        verify(mockMessageManager).displayMessageWithDescription(eq(MessageManager.ERROR), anyString(), anyString());
    }


}
