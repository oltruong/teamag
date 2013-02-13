package fr.oltruong.teamag.webbean;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;

public class ColumnDayBean
    implements Comparable<ColumnDayBean>
{

    private Calendar day;

    private Float total = 0f;

    public Float getTotal()
    {
        return total;
    }

    public void addTotal( Float total )
    {
        this.total += total;
    }

    public void setDay( Calendar day )
    {
        this.day = day;
    }

    public Calendar getDay()
    {
        return day;
    }

    public String getDayHeader()
    {
        return DateFormatUtils.format( day, "E dd" );
    }

    public String getDayNumber()
    {

        return "day" + day.get( Calendar.DAY_OF_WEEK );
    }

    @Override
    public int compareTo( ColumnDayBean arg0 )
    {

        return day.compareTo( arg0.getDay() );
    }
}
