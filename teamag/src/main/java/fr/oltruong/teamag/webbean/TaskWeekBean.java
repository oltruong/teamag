package fr.oltruong.teamag.webbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.Work;

public class TaskWeekBean
{

    private Task task;

    private Calendar month;

    private List<Work> works = new ArrayList<Work>();

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
        System.out.println( "gett " + works.size() );
        return works;
    }

    public void setWorks( List<Work> works )
    {
        this.works = works;
    }

    public void addWork( Work work )
    {
        works.add( work );
    }

}
