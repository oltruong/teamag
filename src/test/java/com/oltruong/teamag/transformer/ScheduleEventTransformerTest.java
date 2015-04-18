package com.oltruong.teamag.transformer;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.utils.MessageManager;
import com.oltruong.teamag.utils.TestUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.primefaces.model.ScheduleEvent;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * @author Olivier Truong
 */
public class ScheduleEventTransformerTest {


    @Mock
    private MessageManager mockMessageManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructorIsPrivate() {
        TestUtils.testConstructorIsPrivate(ScheduleEventTransformer.class);
    }

    @Test
    public void testConvertAbsenceFullDayList() {


        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence);

        long count = 0l;
        for (Absence absence : absenceList) {

            absence.setBeginType(Absence.ALL_DAY);
            absence.setEndType(Absence.ALL_DAY);
            absence.getMember().setId(Long.valueOf(count));
            count++;
        }

        List<ScheduleEvent> eventList = ScheduleEventTransformer.convertAbsenceList(absenceList, mockMessageManager);

        assertThat(eventList).hasSameSizeAs(absenceList);

        for (int i = 0; i < eventList.size(); i++) {
            ScheduleEvent dayOff = eventList.get(i);
            Absence absence = absenceList.get(i);

            assertThat(dayOff.getStartDate()).isEqualTo(absence.getBeginDate().withTimeAtStartOfDay().toDate());
            assertThat(dayOff.getEndDate()).isEqualTo(absence.getEndDate().withTimeAtStartOfDay().toDate());
            assertThat(dayOff.isAllDay()).isTrue();
        }

    }

    @Test
    public void testConvertAbsenceList() {

        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence, 1);
        absenceList.get(0).getMember().setId(Long.valueOf(365l));

        List<ScheduleEvent> eventList = ScheduleEventTransformer.convertAbsenceList(absenceList, mockMessageManager);

        assertThat(eventList).hasSize(3);

        Absence absence = absenceList.get(0);

        ScheduleEvent eventBeginAfternoon = eventList.get(0);
        assertThat(new DateTime(eventBeginAfternoon.getStartDate()).withTimeAtStartOfDay()).isEqualTo(absence.getBeginDate().withTimeAtStartOfDay());
        assertThat(new DateTime(eventBeginAfternoon.getEndDate()).withTimeAtStartOfDay()).isEqualTo(absence.getBeginDate().withTimeAtStartOfDay());


        ScheduleEvent eventEndMorning = eventList.get(1);
        assertThat(new DateTime(eventEndMorning.getStartDate()).withTimeAtStartOfDay()).isEqualTo(absence.getEndDate().withTimeAtStartOfDay());
        assertThat(new DateTime(eventEndMorning.getEndDate()).withTimeAtStartOfDay()).isEqualTo(absence.getEndDate().withTimeAtStartOfDay());

        ScheduleEvent eventBetween = eventList.get(2);
        assertThat(new DateTime(eventBetween.getStartDate()).withTimeAtStartOfDay()).isEqualTo(absence.getBeginDate().withTimeAtStartOfDay().plusDays(1));
        assertThat(new DateTime(eventBetween.getEndDate()).withTimeAtStartOfDay()).isEqualTo(absence.getEndDate().withTimeAtStartOfDay().plusDays(-1));


    }


    @Test
    public void testConvertAbsenceListSingleDayMorning() {
        testConvertAbsenceListSingleDay(Absence.MORNING_ONLY);
    }

    @Test
    public void testConvertAbsenceListSingleDayFullDay() {
        testConvertAbsenceListSingleDay(Absence.ALL_DAY);
    }

    @Test
    public void testConvertAbsenceListSingleDayAfternoon() {
        testConvertAbsenceListSingleDay(Absence.AFTERNOON_ONLY);
    }

    private void testConvertAbsenceListSingleDay(int type) {
        List<Absence> absenceList = createSingleAbsenceList(type);

        Absence absence = absenceList.get(0);

        List<ScheduleEvent> eventList = ScheduleEventTransformer.convertAbsenceList(absenceList, mockMessageManager);

        assertThat(eventList).hasSize(1);
        ScheduleEvent eventBeginMorning = eventList.get(0);
        assertThat(new DateTime(eventBeginMorning.getStartDate()).withTimeAtStartOfDay()).isEqualTo(absence.getBeginDate().withTimeAtStartOfDay());
        assertThat(new DateTime(eventBeginMorning.getEndDate()).withTimeAtStartOfDay()).isEqualTo(absence.getBeginDate().withTimeAtStartOfDay());

    }

    private List<Absence> createSingleAbsenceList(int type) {
        Absence absence = EntityFactory.createAbsence();
        absence.getMember().setId(Long.valueOf(365l));

        absence.setBeginType(type);

        absence.setEndDate(absence.getBeginDate());

        List<Absence> absenceList = Lists.newArrayListWithExpectedSize(1);
        absenceList.add(absence);
        return absenceList;
    }

    @Test
    public void testGetDaysOff() {

        List<ScheduleEvent> daysOff = ScheduleEventTransformer.getDaysOff(mockMessageManager);

        List<DateTime> dateTimeOff = CalendarUtils.getListDaysOff();

        assertThat(daysOff).hasSameSizeAs(dateTimeOff);
        for (int i = 0; i < daysOff.size(); i++) {
            ScheduleEvent dayOff = daysOff.get(i);
            assertThat(dayOff.isAllDay()).isTrue();
            assertThat(dayOff.getStartDate()).isEqualTo(dayOff.getEndDate()).isEqualTo(dateTimeOff.get(i).toDate());
        }

        verify(mockMessageManager).getMessage(eq("dayOff"));
    }
}
