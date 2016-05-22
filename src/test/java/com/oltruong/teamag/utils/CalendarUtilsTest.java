package com.oltruong.teamag.utils;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CalendarUtilsTest {


    public static final int YEAR = 2016;

    @Test
    public void testConstructorIsPrivate() {
        TestUtils.testConstructorIsPrivate(CalendarUtils.class);
    }

    @Test
    public void testGetListDaysOff() {

        List<DateTime> daysOff = CalendarUtils.getListDaysOff();

        assertThat(daysOff).hasSize(11);

        checkContains(daysOff, 1, 1, YEAR);
        checkContains(daysOff, 28, 3, YEAR);
        checkContains(daysOff, 1, 5, YEAR);
        checkContains(daysOff, 8, 5, YEAR);
        checkContains(daysOff, 5, 5, YEAR);
        checkContains(daysOff, 16, 5, YEAR);
        checkContains(daysOff, 14, 7, YEAR);
        checkContains(daysOff, 15, 8, YEAR);

        checkContains(daysOff, 1, 11, YEAR);
        checkContains(daysOff, 11, 11, YEAR);
        checkNotContains(daysOff, 3, 11, YEAR);
        checkContains(daysOff, 25, 12, YEAR);

    }


    @Test
    public void testIsDayOff() {
        assertThat(CalendarUtils.isDayOff(createDay(1, 1, YEAR))).isTrue();
        assertThat(CalendarUtils.isDayOff(createDay(2, 1, YEAR))).isTrue();
        assertThat(CalendarUtils.isDayOff(createDay(3, 1, YEAR))).isTrue();
        assertThat(CalendarUtils.isDayOff(createDay(4, 1, YEAR))).isFalse();
    }

    @Test
    public void testIsWorkingDay() {
        assertThat(CalendarUtils.isWorkingDay(createDay(1, 1, YEAR))).isFalse();
        assertThat(CalendarUtils.isWorkingDay(createDay(3, 1, YEAR))).isFalse();
        assertThat(CalendarUtils.isWorkingDay(createDay(4, 1, YEAR))).isTrue();
        assertThat(CalendarUtils.isWorkingDay(createDay(25, 5, YEAR))).isTrue();
    }

    @Test
    public void testIsLastWorkingDayOfWeek() {


        List<DateTime> workingDays = CalendarUtils.getWorkingDays(createDay(1, 5, YEAR));
        assertThat(CalendarUtils.isLastWorkingDayOfWeek(createDay(6, 5, YEAR), workingDays)).isTrue();
        assertThat(CalendarUtils.isLastWorkingDayOfWeek(createDay(8, 5, YEAR), workingDays)).isFalse();
        assertThat(CalendarUtils.isLastWorkingDayOfWeek(createDay(6, 5, YEAR), workingDays)).isTrue();
        assertThat(CalendarUtils.isLastWorkingDayOfWeek(createDay(13, 5, YEAR), workingDays)).isTrue();
        assertThat(CalendarUtils.isLastWorkingDayOfWeek(createDay(15, 5, YEAR), workingDays)).isFalse();
        assertThat(CalendarUtils.isLastWorkingDayOfWeek(createDay(24, 5, YEAR), workingDays)).isFalse();
    }

    @Test
    public void testIsLastWorkingDayOfMonth() {
        List<DateTime> workingDays = CalendarUtils.getWorkingDays(createDay(1, 5, YEAR));
        assertThat(CalendarUtils.isLastWorkingDayOfMonth(createDay(29, 5, YEAR), workingDays)).isFalse();
        assertThat(CalendarUtils.isLastWorkingDayOfMonth(createDay(30, 5, YEAR), workingDays)).isFalse();
        assertThat(CalendarUtils.isLastWorkingDayOfMonth(createDay(31, 5, YEAR), workingDays)).isTrue();
        assertThat(CalendarUtils.isLastWorkingDayOfMonth(createDay(8, 5, YEAR), workingDays)).isFalse();
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


    public static DateTime createDay(int day, int month, int year) {
        return DateTime.now().withTimeAtStartOfDay().withYear(year).withMonthOfYear(month).withDayOfMonth(day);
    }

    @Test
    public void testGetWorkingDays() {

        testWorkingDays(1, Lists.newArrayList(1));
        testWorkingDays(2, Lists.newArrayList(0));
        testWorkingDays(3, Lists.newArrayList(28));
        testWorkingDays(5, Lists.newArrayList(1, 8));
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

        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(31, 7, YEAR))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(4, 8, YEAR))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(15, 8, YEAR))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(8, 8, YEAR))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 9, YEAR))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(5, 9, YEAR))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 10, YEAR))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(6, 10, YEAR))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 11, YEAR))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(3, 11, YEAR))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 12, YEAR))).isTrue();

    }

    @Test
    public void testIsInLastWorkingWeekOfMonth() throws Exception {

        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(31, 7, YEAR))).isTrue();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(25, 7, YEAR))).isTrue();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(31, 8, YEAR))).isTrue();

    }

    @Test
    public void testGetCurrentWeek() {
        assertThat(CalendarUtils.getCurrentWeekNumber()).isEqualTo(LocalDate.now().get(ChronoField.ALIGNED_WEEK_OF_YEAR));
    }
}
