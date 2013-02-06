package fr.oltruong.teamag.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskMonth
{

    private Task task;

    private Calendar month;

    private List<Work> works = new ArrayList<Work>();

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
        System.out.println( "Calling getWorks " + works.size() );
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
