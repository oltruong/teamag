package com.oltruong.teamag.backingbean;

import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.AbsenceService;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Olivier Truong
 */
public class ScheduleControllerTest extends ControllerTest {

    @Mock
    private AbsenceService mockAbsenceService;

    @Mock
    private MemberService mockMemberService;

    private ScheduleController scheduleController;


    @Before
    public void setup() {
        super.setup();
        scheduleController = new ScheduleController();
        TestUtils.setPrivateAttribute(scheduleController, mockAbsenceService, "absenceService");
        TestUtils.setPrivateAttribute(scheduleController, mockMemberService, "memberService");
        TestUtils.setPrivateAttribute(scheduleController, Controller.class, mockMessageManager, "messageManager");

    }


    @Test
    public void testInit() throws Exception {

        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence);
        for (Absence absence : absenceList) {
            absence.getMember().setId(Long.valueOf(32l));
        }

        when(mockAbsenceService.findAllAbsences()).thenReturn(absenceList);

        String view = scheduleController.init();


        assertThat(view).isEqualTo(TestUtils.getPrivateAttribute(scheduleController, "VIEWNAME"));
        verify(mockAbsenceService).findAllAbsences();
        verify(mockMemberService).findMembers();
        assertThat(scheduleController.getEventModel().getEventCount()).isGreaterThan(0);

    }
}
