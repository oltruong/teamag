package fr.oltruong.teamag.entity;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table( name = "Teamag_work" )
public class Work
{

    @Id
    @GeneratedValue
    private Long id;

    @Temporal( TemporalType.DATE )
    private Calendar month;

    @Temporal( TemporalType.DATE )
    private Calendar day;

    private Member member;

    private Activity activity;

    private Float total = 0f;

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Calendar getMonth()
    {
        return month;
    }

    public void setMonth( Calendar month )
    {
        this.month = month;
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

    public Activity getActivity()
    {
        return activity;
    }

    public void setActivity( Activity activity )
    {
        this.activity = activity;
    }

    public Float getTotal()
    {
        return total;
    }

    public void setTotal( Float total )
    {
        this.total = total;
    }

}
