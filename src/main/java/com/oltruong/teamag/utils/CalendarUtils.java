package com.oltruong.teamag.utils;

import com.google.common.collect.Lists;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

/**
 * @author Olivier Truong
 */
public final class CalendarUtils {

    private CalendarUtils() {
    }

    private static List<LocalDate> listDaysOff;


    public static boolean isWorkingDay(LocalDate day) {
        return !isDayOff(day);
    }

    public static boolean isDayOff(LocalDate day) {
        DayOfWeek dayOfWeek = day.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY) {
            return true;
        } else {
            return getListDaysOff().contains(day);
        }

    }

    public static List<LocalDate> getListDaysOff() {

        if (listDaysOff == null) {
            int numberDaysOff = 11;
            List<LocalDate> daysOff = Lists.newArrayListWithExpectedSize(numberDaysOff);

            daysOff.add(buildDayOff(1, Month.JANUARY));
            daysOff.add(buildDayOff(6, Month.APRIL));
            daysOff.add(buildDayOff(1, Month.MAY));
            daysOff.add(buildDayOff(8, Month.MAY));
            daysOff.add(buildDayOff(14, Month.MAY));
            daysOff.add(buildDayOff(25, Month.MAY));
            daysOff.add(buildDayOff(14, Month.JULY));
            daysOff.add(buildDayOff(15, Month.AUGUST));
            daysOff.add(buildDayOff(1, Month.NOVEMBER));
            daysOff.add(buildDayOff(11, Month.NOVEMBER));
            daysOff.add(buildDayOff(25, Month.DECEMBER));


            listDaysOff = daysOff;
        }
        return listDaysOff;


    }

    private static LocalDate buildDayOff(int day, Month month) {
        return LocalDate.now().withMonth(month.getValue()).withDayOfMonth(day);
    }

    public static List<LocalDate> getWorkingDays(LocalDate month) {
        List<LocalDate> listWorkingDays = Lists.newArrayListWithCapacity(22);
        boolean finished = false;
        LocalDate day = month.withDayOfMonth(1);
        while (!finished) {
            if (!CalendarUtils.isDayOff(day)) {
                listWorkingDays.add(day);
            }
            day = day.plusDays(1);
            finished = day.getMonthValue() != month.getMonthValue();
        }
        return listWorkingDays;

    }


    public static boolean isLastWorkingDayOfWeek(LocalDate day, List<LocalDate> workingDays) {


        if (!workingDays.contains(day)) {
            return false;
        } else {
            int weekNumber = day.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

            LocalDate localDate = day.plusDays(1);
            boolean foundAnotherWorkingDay = false;
            while (localDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == weekNumber && !foundAnotherWorkingDay) {
                foundAnotherWorkingDay = workingDays.contains(localDate);
                localDate = localDate.plusDays(1);
            }
            return !foundAnotherWorkingDay;

        }
    }

    public static boolean isInFirstWorkingWeekOfMonth(LocalDate day) {


        LocalDate firstWorkingDayOfMonth = findFirstWorkingDayMonth(day.getMonthValue());
        return firstWorkingDayOfMonth.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == day.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());


    }

    public static LocalDate findFirstWorkingDayMonth(int monthOfYear) {

        LocalDate dayOfMonth = LocalDate.now().withDayOfMonth(1).withMonth(monthOfYear);

        while (isDayOff(dayOfMonth)) {
            dayOfMonth = dayOfMonth.plusDays(1);
        }

        return dayOfMonth;
    }

    public static boolean isInLastWorkingWeekOfMonth(LocalDate day) {
        LocalDate lastWorkingDayOfMonth = findLastWorkingDayMonth(day.getMonthValue());
        return lastWorkingDayOfMonth.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == day.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }

    private static LocalDate findLastWorkingDayMonth(int monthOfYear) {

        LocalDate dayOfMonth = LocalDate.now().withDayOfMonth(1);

        if (monthOfYear == 12) {
            dayOfMonth = dayOfMonth.plusYears(1).withMonth(1);
        } else {
            dayOfMonth = dayOfMonth.withMonth(monthOfYear + 1);
        }

        dayOfMonth = dayOfMonth.minusDays(1);
        while (isDayOff(dayOfMonth)) {
            dayOfMonth = dayOfMonth.minusDays(1);
        }
        return dayOfMonth;
    }


    public static int getCurrentWeekNumber() {
        return LocalDate.now().get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

}
