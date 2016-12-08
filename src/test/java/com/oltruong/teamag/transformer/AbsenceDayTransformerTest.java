package com.oltruong.teamag.transformer;

import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.model.AbsenceDay;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.utils.TestUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class AbsenceDayTransformerTest {

    @Test
    public void constructorIsPrivate() {
        TestUtils.constructorIsPrivate(AbsenceDayTransformer.class);
    }


    @Test(expected = IllegalArgumentException.class)
    public void transformAbsenceNull() throws Exception {
        AbsenceDayTransformer.transformAbsence(null);

    }

    @Test
    public void transformAbsenceSingleDay() throws Exception {
        DateTime now = getNextDateTimeNotOff();

        Absence absence = EntityFactory.createAbsence(now, Absence.ALL_DAY, now, Absence.ALL_DAY);
        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(absence);

        assertThat(absenceDayList).hasSize(1);
        AbsenceDay absenceDay = absenceDayList.get(0);

        assertThat(absenceDay.getDay()).isEqualTo(now.withTimeAtStartOfDay());
        assertThat(absenceDay.getMember()).isEqualTo(absence.getMember());
        assertThat(absenceDay.getAbsence()).isEqualTo(absence);
        assertThat(absenceDay.getMonth()).isEqualTo(Integer.valueOf(now.getMonthOfYear()));
        assertThat(absenceDay.getWeek()).isEqualTo(Integer.valueOf(now.getWeekOfWeekyear()));
        assertThat(absenceDay.getValue()).isEqualTo(Float.valueOf(1f));
    }

    @Test
    public void transformAbsenceDayOff() throws Exception {
        DateTime now = getNextDateTimeOff();

        Absence absence = EntityFactory.createAbsence(now, Absence.ALL_DAY, now, Absence.ALL_DAY);
        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(absence);

        assertThat(absenceDayList).hasSize(0);

    }


    @Test
    public void transformAbsenceHalfDay() throws Exception {
        DateTime now = getNextDateTimeNotOff();


        Absence absence = EntityFactory.createAbsence(now, Absence.MORNING_ONLY, now, Absence.ALL_DAY);
        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(absence);

        assertThat(absenceDayList).hasSize(1);
        AbsenceDay absenceDay = absenceDayList.get(0);

        assertThat(absenceDay.getDay()).isEqualTo(now.withTimeAtStartOfDay());
        assertThat(absenceDay.getValue()).isEqualTo(Float.valueOf(0.5f));
    }


    @Test
    public void transformAbsenceThreeDays() throws Exception {
        DateTime beginDate = DateTime.now();

        while (next3daysOff(beginDate)) {//Make sure this is not a day off !
            beginDate = beginDate.plusDays(1);
        }

        DateTime threeDaysLater = beginDate.plusDays(3);

        Absence absence = EntityFactory.createAbsence(beginDate, Absence.AFTERNOON_ONLY, threeDaysLater, Absence.MORNING_ONLY);
        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(absence);

        assertThat(absenceDayList).hasSize(4);
        AbsenceDay firstAbsenceDay = absenceDayList.get(0);

        assertThat(firstAbsenceDay.getDay()).isEqualTo(beginDate.withTimeAtStartOfDay());
        assertThat(firstAbsenceDay.getValue()).isEqualTo(Float.valueOf(0.5f));


        AbsenceDay secondAbsenceDay = absenceDayList.get(1);

        assertThat(secondAbsenceDay.getDay()).isEqualTo(beginDate.withTimeAtStartOfDay().plusDays(1));
        assertThat(secondAbsenceDay.getValue()).isEqualTo(Float.valueOf(1f));

        AbsenceDay thirdAbsenceDay = absenceDayList.get(2);

        assertThat(thirdAbsenceDay.getDay()).isEqualTo(beginDate.withTimeAtStartOfDay().plusDays(2));
        assertThat(thirdAbsenceDay.getValue()).isEqualTo(Float.valueOf(1f));

        AbsenceDay lastAbsenceDay = absenceDayList.get(3);

        assertThat(lastAbsenceDay.getDay()).isEqualTo(threeDaysLater.withTimeAtStartOfDay());
        assertThat(lastAbsenceDay.getValue()).isEqualTo(Float.valueOf(0.5f));
    }

    private boolean next3daysOff(DateTime now) {
        DateTime tester = new DateTime(now);
        boolean result = CalendarUtils.isDayOff(tester);
        for (int i = 1; i <= 3; i++) {
            result |= CalendarUtils.isDayOff(now.plusDays(i));

        }
        return result;
    }


    private DateTime getNextDateTimeNotOff() {
        DateTime now = DateTime.now();
        while (CalendarUtils.isDayOff(now)) {
            now = now.plusDays(1);
        }
        return now;
    }


    private DateTime getNextDateTimeOff() {
        DateTime now = DateTime.now();
        while (!CalendarUtils.isDayOff(now)) {
            now = now.plusDays(1);
        }
        return now;
    }
}
