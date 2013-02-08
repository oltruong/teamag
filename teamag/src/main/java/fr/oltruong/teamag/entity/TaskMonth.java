package fr.oltruong.teamag.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table
@NamedQuery( name = "findTaskMonth", query = "SELECT t from TaskMonth t where t.member=:fmember and t.month=:fmonth" )
@Entity
public class TaskMonth
{

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn( nullable = false )
    private Task task;

    @Temporal( TemporalType.DATE )
    @Column( nullable = false )
    private Calendar month;

    private List<Work> works = new ArrayList<Work>();

    @JoinColumn( nullable = false )
    private Member member;

    public Task getTask()
    {
        return task;
    }

    public void setTask( Task task )
    {
        this.task = task;
    }

    public Calendar getMonth()
    {
        return month;
    }

    public void setMonth( Calendar month )
    {
        this.month = month;
    }

    public List<Work> getWorks()
    {
        return works;
    }

    public void setWorks( List<Work> works )
    {
        this.works = works;
    }

    public void addWork( Work work )
    {
        this.works.add( work );
    }

    public Member getMember()
    {
        return member;
    }

    public void setMember( Member member )
    {
        this.member = member;
    }

}
