package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.ejb.AbsenceEJB;
import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.EntityFactory;
import fr.oltruong.teamag.utils.TestUtils;
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
    private AbsenceEJB mockAbsenceEJB;

    @Mock
    private MemberEJB mockMemberEJB;

    private ScheduleController scheduleController;


    @Before
    public void setup() {
        super.setup();
        scheduleController = new ScheduleController();
        TestUtils.setPrivateAttribute(scheduleController, mockAbsenceEJB, "absenceEJB");
        TestUtils.setPrivateAttribute(scheduleController, mockMemberEJB, "memberEJB");
        TestUtils.setPrivateAttribute(scheduleController, Controller.class, mockMessageManager, "messageManager");

    }


    @Test
    public void testInit() throws Exception {

        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence);
        for (Absence absence : absenceList) {
            absence.getMember().setId(Long.valueOf(32l));
        }

        when(mockAbsenceEJB.findAllAbsences()).thenReturn(absenceList);

        String view = scheduleController.init();


        assertThat(view).isEqualTo(TestUtils.getPrivateAttribute(scheduleController, "VIEWNAME"));
        verify(mockAbsenceEJB).findAllAbsences();
        verify(mockMemberEJB).findMembers();
        assertThat(scheduleController.getEventModel().getEventCount()).isGreaterThan(0);

    }
}
