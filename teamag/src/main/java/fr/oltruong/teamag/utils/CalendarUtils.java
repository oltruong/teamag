package fr.oltruong.teamag.utils;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.List;

public final class CalendarUtils {

    private CalendarUtils() {
    }

    private static boolean isDayOff(DateTime day) {
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
                    verdict = dayOfMonth == 1;
                    break;
                case DateTimeConstants.MAY:
                    verdict = (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9 || dayOfMonth == 20);
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

}
