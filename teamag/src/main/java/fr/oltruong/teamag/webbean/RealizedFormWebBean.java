package fr.oltruong.teamag.webbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import fr.oltruong.teamag.utils.CalendarUtils;

public class RealizedFormWebBean
{
    private Calendar dayCursor;

    private Calendar currentMonth;

    private List<ColumnDayBean> columnsDay = new ArrayList<ColumnDayBean>( 5 );

    private List<TaskWeekBean> taskWeeks = new ArrayList<TaskWeekBean>();

    public boolean getIsFirstWeek()
    {
        return CalendarUtils.isFirstWeek( dayCursor );
    }

    public boolean getIsLastWeek()
    {
        return CalendarUtils.isLastWeek( dayCursor );
    }

    public void addColumnDay( ColumnDayBean columnDay )
    {
        columnsDay.add( columnDay );
    }

    public List<ColumnDayBean> getColumnsDay()
    {
        return columnsDay;
    }

    public Integer getWeekNumber()
    {
        return dayCursor.get( Calendar.WEEK_OF_YEAR );
    }

    public List<TaskWeekBean> getTaskWeeks()
    {
        return taskWeeks;
    }

    public void setTaskWeeks( List<TaskWeekBean> taskWeeks )
    {
        this.taskWeeks = taskWeeks;
    }

    public void incrementWeek()
    {
        dayCursor.add( Calendar.WEEK_OF_MONTH, 1 );

    }

    public void decrementWeek()
    {
        dayCursor.add( Calendar.WEEK_OF_MONTH, -1 );
    }

    public Calendar getCurrentMonth()
    {
        return currentMonth;
    }

    public String getCurrentMonthStr()
    {
        return DateFormatUtils.format( currentMonth, "MMMMM" );
    }

    public void setCurrentMonth( Calendar currentMonth )
    {
        this.currentMonth = currentMonth;
    }

    public void setDayCursor( Calendar dayCursor )
    {
        this.dayCursor = dayCursor;
    }

}
