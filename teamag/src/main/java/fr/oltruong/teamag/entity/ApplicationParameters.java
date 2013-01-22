package fr.oltruong.teamag.entity;

import java.util.Calendar;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ApplicationParameters
{

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @Temporal( TemporalType.DATE )
    private List<Calendar> daysOff;

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public List<Calendar> getDaysOff()
    {
        return daysOff;
    }

    public void setDaysOff( List<Calendar> daysOff )
    {
        this.daysOff = daysOff;
    }

}
