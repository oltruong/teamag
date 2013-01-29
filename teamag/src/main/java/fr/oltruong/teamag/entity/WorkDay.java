package fr.oltruong.teamag.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.time.DateFormatUtils;

@Entity
@NamedQueries( {
    @NamedQuery( name = "findWorkDaysByMember", query = "SELECT w from WorkDay w where w.member=:fmember order by w.day " ),
    @NamedQuery( name = "findWorkDaysByName", query = "SELECT m from Member m where m.name=:fname" ) } )
public class WorkDay
{

    @Id
    @GeneratedValue
    private Long id;

    @Temporal( TemporalType.DATE )
    private Calendar month;

    @Temporal( TemporalType.DATE )
    private Calendar day;

    private Member member;

    private Float absenceRate = 0f;

    private List<Work> works = new ArrayList<Work>();

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

    public Calendar getMonth()
    {
        return month;
    }

    public void setMonth( Calendar month )
    {
        this.month = month;
    }

    public Member getMember()
    {
        return member;
    }

    public void setMember( Member member )
    {
        this.member = member;
    }

    public void addWork( Work work )
    {
        this.works.add( work );
    }

    public Float getAbsenceRate()
    {
        return absenceRate;
    }

    public void setAbsenceRate( Float absenceRate )
    {
        this.absenceRate = absenceRate;
    }

    public String getDayStr()
    {
        return DateFormatUtils.format( getDay(), "dd/MM" );
    }

    public List<Work> getWorks()
    {
        System.err.println( "AAAAAAAAAAAAAAAA" );
        return works;
    }

    public void setWorks( List<Work> works )
    {
        this.works = works;
    }

}
