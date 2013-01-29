package fr.oltruong.teamag.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@NamedQuery( name = "findAllActivities", query = "SELECT a from Activity a order by a.name, a.project" )
@Entity
public class Activity
{
    @Id
    @GeneratedValue
    private Long id;

    @Column( nullable = false )
    private String name;

    private String project;

    // @OneToMany
    // @JoinTable( name = "jnd_activity_member", joinColumns = @JoinColumn( name = "activity_fk" ), inverseJoinColumns =
    // @JoinColumn( name = "member_fk" ) )
    private List<Member> members = new ArrayList<Member>( 1 );

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

}
