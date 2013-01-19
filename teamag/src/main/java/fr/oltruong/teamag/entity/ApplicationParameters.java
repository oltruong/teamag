package fr.oltruong.teamag.entity;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class ApplicationParameters
{

    private List<Calendar> daysOff;

    public List<Calendar> getDaysOff()
    {
        return daysOff;
    }

    public void setDaysOff( List<Calendar> daysOff )
    {
        this.daysOff = daysOff;
    }

}
