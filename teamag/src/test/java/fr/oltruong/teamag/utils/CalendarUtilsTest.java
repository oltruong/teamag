package fr.oltruong.teamag.utils;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Calendar;

import org.junit.Test;

public class CalendarUtilsTest {

    @Test
    public void testGetFirstDayOfMonth() {

        Calendar cal = Calendar.getInstance();
        assertThat(CalendarUtils.getFirstDayOfMonth(cal).get(Calendar.DAY_OF_MONTH)).isEqualTo(1);
    }

    @Test
    public void testGetFirstDayOfMonth_null() {
        assertThat(CalendarUtils.getFirstDayOfMonth(null)).isNull();
    }

    @Test
    public void testGetNextDay() {
        Calendar cal = Calendar.getInstance();
        assertThat(CalendarUtils.getNextDay(cal).get(Calendar.DAY_OF_YEAR)).isEqualTo((cal.get(Calendar.DAY_OF_YEAR) + 1));
    }

    @Test
    public void testGetNextDay_null() {
        assertThat(CalendarUtils.getNextDay(null)).isNull();
    }

    @Test
    public void testGetPreviousMonth() {
        Calendar cal = Calendar.getInstance();
        assertThat(CalendarUtils.getPreviousMonth(cal).get(Calendar.MONTH)).isEqualTo((cal.get(Calendar.MONTH) - 1));
    }

    @Test
    public void testGetPreviousMonth_null() {
        assertThat(CalendarUtils.getPreviousMonth(null)).isNull();
    }

}
