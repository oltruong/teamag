package fr.oltruong.teamag.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class CalendarUtils
{

    private CalendarUtils()
    {

    }

    public static Calendar getFirstDayOfMonth( Calendar date )
    {
        if ( date != null )
        {
            Calendar newDate = (Calendar) date.clone();
            newDate.set( Calendar.DAY_OF_MONTH, 1 );
            return newDate;
        }
        return null;
    }

    public static Calendar getNextDay( Calendar date )
    {
        if ( date != null )
        {
            Calendar newDate = (Calendar) date.clone();
            newDate.add( Calendar.DAY_OF_YEAR, 1 );
            return newDate;
        }
        return null;
    }

    public static Calendar getPreviousMonth( Calendar date )
    {
        if ( date != null )
        {
            Calendar newDate = (Calendar) date.clone();
            newDate.add( Calendar.MONTH, -1 );
            return newDate;
        }
        return null;
    }

    private static boolean isDayOff( Calendar day )
    {
        boolean verdict = false;

        int dayOfWeek = day.get( Calendar.DAY_OF_WEEK );
        if ( dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY )
        {
            return true;
        }
        else
        {
            int dayOfMonth = day.get( Calendar.DAY_OF_MONTH );
            // Days off
            switch ( day.get( Calendar.MONTH ) )
            {
                case Calendar.APRIL:
                    verdict = dayOfMonth == 1;
                    break;
                case Calendar.MAY:
                    verdict = ( dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9 || dayOfMonth == 20 );
                    break;
                case Calendar.AUGUST:
                    verdict = ( dayOfMonth == 15 );
                    break;
                case Calendar.NOVEMBER:
                    verdict = ( dayOfMonth == 1 || dayOfMonth == 11 );
                    break;
                case Calendar.DECEMBER:
                    verdict = ( dayOfMonth == 25 );
                    break;
                default:
                    break;
            }
        }
        return verdict;

    }

    public static List<Calendar> getWorkingDays( Calendar month )
    {
        List<Calendar> listWorkingDays = new ArrayList<Calendar>( 22 );
        boolean finished = false;
        Calendar day = CalendarUtils.getFirstDayOfMonth( month );
        while ( !finished )
        {
            if ( !CalendarUtils.isDayOff( day ) )
            {
                listWorkingDays.add( day );
            }
            day = CalendarUtils.getNextDay( day );
            finished = day.get( Calendar.MONTH ) != month.get( Calendar.MONTH );
        }
        return listWorkingDays;

    }

}
