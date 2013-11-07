package fr.oltruong.teamag.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CalendarUtilsTest {

    @Test
    public void testGetWorkingDaysJanuary() {

        testWorkingDays(1, Lists.newArrayList(1));
        testWorkingDays(2, Lists.newArrayList(0));
        testWorkingDays(4, Lists.newArrayList(1));
        testWorkingDays(5, Lists.newArrayList(1, 8, 9, 20));
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
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<CalendarUtils> constructor = CalendarUtils.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

}
