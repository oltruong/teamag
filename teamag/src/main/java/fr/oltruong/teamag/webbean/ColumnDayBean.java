package fr.oltruong.teamag.webbean;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;

public class ColumnDayBean
{

    private Calendar day;

    public void setDay( Calendar day )
    {
        this.day = day;
    }

    public String getDayHeader()
    {
        return DateFormatUtils.format( day, "E dd" );
    }

    public String getDayNumber()
    {

        return "day" + day.get( Calendar.DAY_OF_WEEK );
    }
}
