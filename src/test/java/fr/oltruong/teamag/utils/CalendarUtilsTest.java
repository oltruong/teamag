package fr.oltruong.teamag.utils;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CalendarUtilsTest {


    @Test
    public void testConstructorIsPrivate() {
        TestUtils.testConstructorIsPrivate(CalendarUtils.class);
    }

    @Test
    public void testGetListDaysOff() {
        List<DateTime> daysOff = CalendarUtils.getListDaysOff();

        assertThat(daysOff).hasSize(11);

        checkContains(daysOff, 1, 1, 2015);
        checkContains(daysOff, 6, 4, 2015);
        checkContains(daysOff, 1, 5, 2015);
        checkContains(daysOff, 8, 5, 2015);
        checkContains(daysOff, 14, 5, 2015);
        checkContains(daysOff, 25, 5, 2015);
        checkContains(daysOff, 14, 7, 2015);
        checkContains(daysOff, 15, 8, 2015);

        checkContains(daysOff, 1, 11, 2015);
        checkContains(daysOff, 11, 11, 2015);
        checkNotContains(daysOff, 3, 11, 2015);
        checkContains(daysOff, 25, 12, 2015);

    }


    @Test
    public void testIsDayOff() {
        assertThat(CalendarUtils.isDayOff(createDay(1, 1, 2015))).isTrue();
        assertThat(CalendarUtils.isDayOff(createDay(2, 1, 2015))).isFalse();
        assertThat(CalendarUtils.isDayOff(createDay(3, 1, 2015))).isTrue();
        assertThat(CalendarUtils.isDayOff(createDay(4, 1, 2015))).isTrue();
        assertThat(CalendarUtils.isDayOff(createDay(25, 5, 2015))).isTrue();
    }

    private void checkContains(List<DateTime> daysOff, int day, int month, int year) {
        assertThat(contains(daysOff, day, month, year)).isTrue();
    }

    private void checkNotContains(List<DateTime> daysOff, int day, int month, int year) {
        assertThat(contains(daysOff, day, month, year)).isFalse();
    }

    private boolean contains(List<DateTime> daysOff, int day, int month, int year) {
        DateTime dayOffCheck = createDay(day, month, year);
        boolean found = false;
        for (DateTime dayOff : daysOff) {
            if (dayOff.isEqual(dayOffCheck)) {
                found = true;
                break;
            }
        }
        return found;
    }


    private DateTime createDay(int day, int month, int year) {
        return DateTime.now().withTimeAtStartOfDay().withYear(year).withMonthOfYear(month).withDayOfMonth(day);
    }

    @Test
    public void testGetWorkingDays() {

        testWorkingDays(1, Lists.newArrayList(1));
        testWorkingDays(2, Lists.newArrayList(0));
        testWorkingDays(4, Lists.newArrayList(6));
        testWorkingDays(5, Lists.newArrayList(1, 8, 14, 25));
        testWorkingDays(6, Lists.newArrayList(0));
        testWorkingDays(7, Lists.newArrayList(14));
        testWorkingDays(8, Lists.newArrayList(15));
        testWorkingDays(11, Lists.newArrayList(1, 11));
        testWorkingDays(12, Lists.newArrayList(25));

    }

    private void testWorkingDays(int monthNumber, Iterable<Integer> values) {
        DateTime month = DateTime.now().withMonthOfYear(monthNumber);
        List<DateTime> workingDays = CalendarUtils.getWorkingDays(month);
        assertThat(workingDays).isNotNull();
        for (DateTime day : workingDays) {
            assertThat(day.getDayOfWeek()).isNotEqualTo(DateTimeConstants.SUNDAY).isNotEqualTo(DateTimeConstants.SATURDAY);
            assertThat(Integer.valueOf(day.getDayOfMonth())).isNotIn(values);

        }
    }


    @Test
    public void testIsInFirstWorkingWeekOfMonth() throws Exception {

        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(31, 7, 2015))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(4, 8, 2015))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(15, 8, 2015))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 8, 2015))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 9, 2015))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(5, 9, 2015))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 10, 2015))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(6, 10, 2015))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 11, 2015))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(3, 11, 2015))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 12, 2015))).isTrue();

    }

    @Test
    public void testIsInLastWorkingWeekOfMonth() throws Exception {

        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(31, 7, 2015))).isTrue();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(25, 7, 2015))).isFalse();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(31, 8, 2015))).isTrue();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(26, 12, 2015))).isFalse();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(29, 12, 2015))).isTrue();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(31, 12, 2015))).isTrue();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(29, 5, 2015))).isTrue();

    }

    @Test
    public void testGetCurrentWeek() {
        assertThat(CalendarUtils.getCurrentWeekNumber()).isEqualTo(LocalDate.now().get(ChronoField.ALIGNED_WEEK_OF_YEAR));
    }
}
