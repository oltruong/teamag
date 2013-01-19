package fr.oltruong.teamag.entity;

import java.util.Calendar;

import javax.persistence.Entity;

@Entity
public class WorkDay
{

    private Calendar day;

    private Member member;

    private Float workRate = 1f;

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
