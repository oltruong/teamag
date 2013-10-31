package fr.oltruong.teamag.utils;

import static org.assertj.core.api.Assertions.assertThat;


import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

public class CalendarUtilsTest {

    @Test
    public void testGetWorkingDays() {

        DateTime january= DateTime.now().withMonthOfYear(1);
        List <DateTime> workingDays = CalendarUtils.getWorkingDays(january);
        assertThat(workingDays).isNotNull();
        for (DateTime day:workingDays){
            assertThat(day.getDayOfWeek()).isNotEqualTo(DateTimeConstants.SUNDAY).isNotEqualTo(DateTimeConstants.SATURDAY);
            assertThat(day.getDayOfMonth()).isNotEqualTo(1);
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
