package fr.oltruong.teamag.webbean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.Work;

public class TaskWeekBean
    implements Comparable<TaskWeekBean>
{

    private Task task;

    private Map<String, Work> mapWorks = new HashMap<String, Work>( 5 );

    public Task getTask()
    {
        return task;
    }

    public void setTask( Task task )
    {
        this.task = task;
    }

    public Collection<Work> getWorks()
    {
        return mapWorks.values();
    }

    private Work getWork( String columnDay )
    {

        return mapWorks.get( columnDay );
    }

    public void addWork( String column, Work work )
    {
        mapWorks.put( column, work );
    }

    public Work getDay6()
    {
        return getWork( "day6" );
    }

    public Work getDay2()
    {
        return getWork( "day2" );
    }

    public Work getDay3()
    {
        return getWork( "day3" );
    }

    public Work getDay4()
    {
        return getWork( "day4" );
    }

    public Work getDay5()
    {
        return getWork( "day5" );
    }

    @Override
    public int compareTo( TaskWeekBean otherTask )
    {

        return task.compareTo( otherTask.getTask() );
    }

}
