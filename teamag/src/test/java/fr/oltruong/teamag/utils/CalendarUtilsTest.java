package fr.oltruong.teamag.utils;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

public class CalendarUtilsTest
{

    @Test
    public void testIsFirstWeek()
    {

        Calendar firstFeb = (Calendar) Calendar.getInstance().clone();
        firstFeb.set( 2013, Calendar.FEBRUARY, 1 );
        // System.out.println( firstFeb.getTime() );
        assertTrue( "first feb should be the first week", CalendarUtils.isFirstWeek( firstFeb ) );

    }

    @Test
    public void testIsLastWeek()
    {

        Calendar lastFeb = (Calendar) Calendar.getInstance().clone();

        lastFeb.set( 2013, Calendar.FEBRUARY, 28 );
        // System.out.println( lastFeb.getTime() );

        assertTrue( "28 feb should be the last week", CalendarUtils.isLastWeek( lastFeb ) );

    }
}
