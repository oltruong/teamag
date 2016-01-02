package com.oltruong.teamag.utils;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;
import org.joda.time.base.BaseDateTime;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.function.Function;

/**
 * @author Olivier Truong
 */
public final class CalendarUtils {

    private CalendarUtils() {
    }

    private static List<DateTime> listDaysOff;


    public static boolean isWorkingDay(DateTime day) {
        return !isDayOff(day);
    }

    public static boolean isDayOff(DateTime day) {
        boolean verdict;

        int dayOfWeek = day.getDayOfWeek();
        if (dayOfWeek == DateTimeConstants.SUNDAY || dayOfWeek == DateTimeConstants.SATURDAY) {
            return true;
        } else {
            verdict = getListDaysOff().contains(day.withTimeAtStartOfDay());
        }
        return verdict;

    }

    public static List<DateTime> getListDaysOff() {

        if (listDaysOff == null) {
            int numberDaysOff = 11;
            List<DateTime> daysOff = Lists.newArrayListWithExpectedSize(numberDaysOff);

            daysOff.add(buildDayOff(1, DateTimeConstants.JANUARY));
            daysOff.add(buildDayOff(28, DateTimeConstants.MARCH));
            daysOff.add(buildDayOff(1, DateTimeConstants.MAY));
            daysOff.add(buildDayOff(8, DateTimeConstants.MAY));
            daysOff.add(buildDayOff(5, DateTimeConstants.MAY));
            daysOff.add(buildDayOff(16, DateTimeConstants.MAY));
            daysOff.add(buildDayOff(14, DateTimeConstants.JULY));
            daysOff.add(buildDayOff(15, DateTimeConstants.AUGUST));
            daysOff.add(buildDayOff(1, DateTimeConstants.NOVEMBER));
            daysOff.add(buildDayOff(11, DateTimeConstants.NOVEMBER));
            daysOff.add(buildDayOff(25, DateTimeConstants.DECEMBER));


            listDaysOff = daysOff;
        }
        return listDaysOff;


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


    public static boolean isLastWorkingDayOfWeek(DateTime day, List<DateTime> workingDays) {
        return isLastWorkingDayOf(day, workingDays, BaseDateTime::getWeekOfWeekyear);
    }

    public static boolean isLastWorkingDayOfMonth(DateTime day, List<DateTime> workingDays) {
        return isLastWorkingDayOf(day, workingDays, BaseDateTime::getMonthOfYear);
    }


    public static boolean isLastWorkingDayOf(DateTime day, List<DateTime> workingDays, Function<BaseDateTime, Integer> function) {
        if (!workingDays.contains(day)) {
            return false;
        } else {
            int unitNumber = function.apply(day);
            MutableDateTime mutableDateTime = new MutableDateTime(day);
            mutableDateTime.addDays(1);
            boolean foundAnotherWorkingDay = false;
            while (function.apply(mutableDateTime) == unitNumber && !foundAnotherWorkingDay) {
                foundAnotherWorkingDay = workingDays.contains(mutableDateTime.toDateTime());
                mutableDateTime.addDays(1);
            }
            return !foundAnotherWorkingDay;

        }
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


    public static int getCurrentWeekNumber() {
        return LocalDate.now().get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

}
