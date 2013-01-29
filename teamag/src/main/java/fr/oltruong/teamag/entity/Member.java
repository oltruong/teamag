package fr.oltruong.teamag.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery( name = "findMembers", query = "SELECT m from Member m order by m.name" ),
    @NamedQuery( name = "findByName", query = "SELECT m from Member m where m.name=:fname" ) } )
public class Member
    implements Serializable
{

    @Id
    @GeneratedValue
    private Long id;

    @Column( nullable = false )
    private String name;

    @Column( nullable = false )
    private String company;

    @Column( nullable = false )
    private Float productivity = 1f;

    @ElementCollection
    @Temporal( TemporalType.DATE )
    private List<Calendar> daysOff;

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

    public String getCompany()
    {
        return company;
    }

    public void setCompany( String company )
    {
        this.company = company;
    }

}
