package fr.oltruong.teamag.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.time.DateFormatUtils;

@Table( name = "TM_WORK" )
@Entity
@NamedQuery( name = "findWorksByMember", query = "SELECT w from Work w where w.member.name=:fmemberName and w.month=:fmonth order by w.task.name, w.day" )
public class Work
{

    @Id
    @GeneratedValue
    private Long id;

    @Column( nullable = false )
    @Temporal( TemporalType.DATE )
    private Calendar month;

    @Column( nullable = false )
    @Temporal( TemporalType.DATE )
    private Calendar day;

    @JoinColumn( nullable = false )
    private Member member;

    @JoinColumn( nullable = false )
    private Task task;

    private Float total = 0f;

    @Transient
    private Float totalEdit = null;

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

    public Task getTask()
    {
        return task;
    }

    public void setTask( Task task )
    {
        this.task = task;
    }

    public Float getTotal()
    {
        return total;
    }

    public void setTotal( Float total )
    {
        this.total = total;
        this.totalEdit = total;
    }

    public Float getTotalEdit()
    {
        if ( totalEdit == null )
        {
            totalEdit = total;
        }
        return totalEdit;
    }

    public void setTotalEdit( Float totalEdit )
    {

        this.totalEdit = totalEdit;
    }

    public String getDayStr()
    {
        return DateFormatUtils.format( getDay(), "E dd" );
    }

    public boolean hasChanged()
    {
        return total.floatValue() != totalEdit.floatValue();
    }

}
