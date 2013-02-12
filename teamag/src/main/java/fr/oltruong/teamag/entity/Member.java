package fr.oltruong.teamag.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table( name = "TM_MEMBER" )
@Entity
@NamedQueries( { @NamedQuery( name = "findMembers", query = "SELECT m from Member m order by m.name" ),
    @NamedQuery( name = "findByName", query = "SELECT m from Member m where m.name=:fname" ) } )
public class Member
    implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column( nullable = false, unique = true )
    private String name;

    @Column( nullable = false )
    private String company;

    @Column( nullable = false )
    private String email;

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

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    @Override
    public boolean equals( Object arg0 )
    {
        Member member0 = (Member) arg0;
        return this.id.equals( member0.getId() );
    }

}
