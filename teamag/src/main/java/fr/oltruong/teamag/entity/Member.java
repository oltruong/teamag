package fr.oltruong.teamag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery( name = "findMembers", query = "SELECT m from Member m order by m.name" )
public class Member
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
