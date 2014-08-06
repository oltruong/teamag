package fr.oltruong.teamag.utils;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;

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

        checkContains(daysOff, 1, 1);
        checkContains(daysOff, 21, 4);
        checkContains(daysOff, 1, 5);
        checkContains(daysOff, 8, 5);
        checkContains(daysOff, 29, 5);
        checkContains(daysOff, 9, 6);
        checkContains(daysOff, 14, 7);
        checkContains(daysOff, 15, 8);

        checkContains(daysOff, 1, 11);
        checkContains(daysOff, 11, 11);
        checkNotContains(daysOff, 3, 11);
        checkContains(daysOff, 25, 12);

    }

    private void checkContains(List<DateTime> daysOff, int day, int month) {
        assertThat(contains(daysOff, day, month)).isTrue();
    }

    private void checkNotContains(List<DateTime> daysOff, int day, int month) {
        assertThat(contains(daysOff, day, month)).isFalse();
    }

    private boolean contains(List<DateTime> daysOff, int day, int month) {
        DateTime dayOffCheck = createDay(day, month);
        boolean found = false;
        for (DateTime dayOff : daysOff) {
            if (dayOff.isEqual(dayOffCheck)) {
                found = true;
                break;
            }
        }
        return found;
    }


    private DateTime createDay(int day, int month) {
        return DateTime.now().withTimeAtStartOfDay().withMonthOfYear(month).withDayOfMonth(day);
    }

    @Test
    public void testGetWorkingDaysJanuary() {

        testWorkingDays(1, Lists.newArrayList(1));
        testWorkingDays(2, Lists.newArrayList(0));
        testWorkingDays(4, Lists.newArrayList(21));
        testWorkingDays(5, Lists.newArrayList(1, 8, 29));
        testWorkingDays(6, Lists.newArrayList(9));
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

        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(31, 7))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(4, 8))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(15, 8))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 8))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 9))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(5, 9))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 10))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(6, 10))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 11))).isFalse();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(3, 11))).isTrue();
        assertThat(CalendarUtils.isInFirstWorkingWeekOfMonth(createDay(1, 12))).isTrue();

    }

    @Test
    public void testIsInLastWorkingWeekOfMonth() throws Exception {

        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(31, 7))).isTrue();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(25, 7))).isFalse();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(25, 8))).isTrue();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(26, 12))).isFalse();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(29, 12))).isTrue();
        assertThat(CalendarUtils.isInLastWorkingWeekOfMonth(createDay(31, 12))).isTrue();

    }
}
