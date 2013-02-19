package fr.oltruong.teamag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table( name = "TM_ACTIVITY" )
@Entity
@NamedQueries( {
    @NamedQuery( name = "findAllActivities", query = "SELECT a from Activity a order by a.bc.number, a.name" ),
    @NamedQuery( name = "findActivity", query = "SELECT a from Activity a where a.name=:fname and a.bc=:fbc" ) } )
public class Activity
{

    @Id
    @GeneratedValue
    private Long id;

    @Column( nullable = false )
    private String name;

    @JoinColumn( nullable = false )
    private BusinessCase bc = new BusinessCase();

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

    public BusinessCase getBc()
    {
        return bc;
    }

    public void setBc( BusinessCase bc )
    {
        System.out.println( "aaaaa " + bc.getName() );
        this.bc = bc;
    }

}
