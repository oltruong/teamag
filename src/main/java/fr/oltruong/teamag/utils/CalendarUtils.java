package fr.oltruong.teamag.utils;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;

/**
 * @author Olivier Truong
 */
public final class CalendarUtils {

    private CalendarUtils() {
    }

    public static boolean isDayOff(DateTime day) {
        boolean verdict = false;

        int dayOfWeek = day.getDayOfWeek();
        if (dayOfWeek == DateTimeConstants.SUNDAY || dayOfWeek == DateTimeConstants.SATURDAY) {
            return true;
        } else {
            int dayOfMonth = day.getDayOfMonth();
            // Days off
            switch (day.getMonthOfYear()) {
                case DateTimeConstants.JANUARY:
                    verdict = dayOfMonth == 1;
                    break;
                case DateTimeConstants.APRIL:
                    verdict = dayOfMonth == 21;
                    break;
                case DateTimeConstants.MAY:
                    verdict = (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 29);
                    break;
                case DateTimeConstants.JUNE:
                    verdict = dayOfMonth == 9;
                    break;
                case DateTimeConstants.JULY:
                    verdict = dayOfMonth == 14;
                    break;
                case DateTimeConstants.AUGUST:
                    verdict = (dayOfMonth == 15);
                    break;
                case DateTimeConstants.NOVEMBER:
                    verdict = (dayOfMonth == 1 || dayOfMonth == 11);
                    break;
                case DateTimeConstants.DECEMBER:
                    verdict = (dayOfMonth == 25);
                    break;
                default:
                    break;
            }
        }
        return verdict;

    }

    public static List<DateTime> getListDaysOff() {
        int numberDaysOff = 11;
        List<DateTime> daysOff = Lists.newArrayListWithExpectedSize(numberDaysOff);

        daysOff.add(buildDayOff(1, DateTimeConstants.JANUARY));
        daysOff.add(buildDayOff(21, DateTimeConstants.APRIL));
        daysOff.add(buildDayOff(1, DateTimeConstants.MAY));
        daysOff.add(buildDayOff(8, DateTimeConstants.MAY));
        daysOff.add(buildDayOff(29, DateTimeConstants.MAY));
        daysOff.add(buildDayOff(9, DateTimeConstants.JUNE));
        daysOff.add(buildDayOff(14, DateTimeConstants.JULY));
        daysOff.add(buildDayOff(15, DateTimeConstants.AUGUST));
        daysOff.add(buildDayOff(1, DateTimeConstants.NOVEMBER));
        daysOff.add(buildDayOff(11, DateTimeConstants.NOVEMBER));
        daysOff.add(buildDayOff(25, DateTimeConstants.DECEMBER));


        return daysOff;
    }

    private static DateTime buildDayOff(int day, int month) {
        return DateTime.now().withTimeAtStartOfDay().withMonthOfYear(month).withDayOfMonth(day);
    }

    public static List<DateTime> getWorkingDays(DateTime month) {
        List<DateTime> listWorkingDays = Lists.newArrayListWithCapacity(22);
        boolean finished = false;
        DateTime day = month.withDayOfMonth(1).withTimeAtStartOfDay();
        while (!finished) {
            if (!CalendarUtils.isDayOff(day)) {
                listWorkingDays.add(day);
            }
            day = day.plusDays(1);
            finished = day.getMonthOfYear() != month.getMonthOfYear();
        }
        return listWorkingDays;

    }

    public static boolean isInFirstWorkingWeekOfMonth(DateTime day) {


        DateTime firstWorkingDayOfMonth = findFirstWorkingDayMonth(day.getMonthOfYear());
        return firstWorkingDayOfMonth.getWeekOfWeekyear() == day.getWeekOfWeekyear();


    }

    public static DateTime findFirstWorkingDayMonth(int monthOfYear) {

        MutableDateTime dayOfMonth = MutableDateTime.now();
        dayOfMonth.setDayOfMonth(1);
        dayOfMonth.setMonthOfYear(monthOfYear);

        while (isDayOff(dayOfMonth.toDateTime())) {
            dayOfMonth.addDays(1);
        }

        return dayOfMonth.toDateTime();
    }

    public static boolean isInLastWorkingWeekOfMonth(DateTime day) {


        DateTime lastWorkingDayOfMonth = findLastWorkingDayMonth(day.getMonthOfYear());
        return lastWorkingDayOfMonth.getWeekOfWeekyear() == day.getWeekOfWeekyear();


    }

    private static DateTime findLastWorkingDayMonth(int monthOfYear) {

        MutableDateTime dayOfMonth = MutableDateTime.now();
        dayOfMonth.setDayOfMonth(1);
        if (monthOfYear == 12) {
            dayOfMonth.addYears(1);
            dayOfMonth.setMonthOfYear(1);
        } else {
            dayOfMonth.setMonthOfYear(monthOfYear + 1);
        }

        dayOfMonth.addDays(-1);
        while (isDayOff(dayOfMonth.toDateTime())) {
            dayOfMonth.addDays(-1);
        }

        return dayOfMonth.toDateTime();
    }


    public static int getCurrentWeekNumber(){
        return LocalDate.now().get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

}
