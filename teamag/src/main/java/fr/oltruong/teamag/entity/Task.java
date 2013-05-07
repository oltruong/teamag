package fr.oltruong.teamag.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table( name = "TM_TASK" )
@NamedQueries( { @NamedQuery( name = "findAllTasks", query = "SELECT t from Task t order by t.name, t.project" ),
    @NamedQuery( name = "findTaskByName", query = "SELECT t from Task t where (t.name=:fname and t.project=:fproject)" ) } )
@Entity
public class Task
{
    @Id
    @GeneratedValue
    private Long id;

    @Column( nullable = false )
    private String name;

    private String project = "";

    private List<Member> members = new ArrayList<Member>( 1 );

    @Transient
    private Float total;

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getProject()
    {
        return project;
    }

    public void setProject( String project )
    {
        this.project = project;
    }

    public List<Member> getMembers()
    {
        return members;
    }

    public void setMembers( List<Member> members )
    {
        this.members = members;
    }

    public void addMember( Member member )
    {
        if ( !this.members.contains( member ) )
        {
            this.members.add( member );
        }
    }

    public Float getTotal()
    {
        return total;
    }

    public void setTotal( Float total )
    {
        this.total = total;
    }

    public void addTotal( Float value )
    {
        if ( total == null )
        {
            total = value;
        }
        else
        {
            total += value;
        }
    }

    public int compareTo( Task task )
    {

        return ( project + name ).compareTo( task.project + task.name );
    }

    @Override
    public boolean equals( Object otherTask )
    {
        if ( !( otherTask instanceof Task ) )
        {
            return false;
        }
        Task member0 = (Task) otherTask;
        return this.id.equals( member0.getId() );
    }

    @Override
    public Task clone()
    {
        Task cloneTask = new Task();
        cloneTask.setId( id );
        cloneTask.setName( name );
        cloneTask.setProject( project );
        return cloneTask;

    }

}
