package fr.oltruong.teamag.entity;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class WorkDay
{

    @Id
    @GeneratedValue
    private Long id;

    @Temporal( TemporalType.DATE )
    private Calendar day;

    private Member member;

    private Float workRate = 1f;

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Calendar getDay()
    {
        return day;
    }

    public void setDay( Calendar day )
    {
        this.day = day;
    }

    public Member getMember()
    {
        return member;
    }

    public void setMember( Member member )
    {
        this.member = member;
    }

    public Float getWorkRate()
    {
        return workRate;
    }

    public void setWorkRate( Float workRate )
    {
        this.workRate = workRate;
    }

}
