package fr.oltruong.teamag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table( name = "TM_BUSINESSCASE" )
@Entity
public class BusinessCase
{

    @Id
    private Integer number;

    @Column( nullable = false, unique = true )
    private String name;

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber( Integer number )
    {
        this.number = number;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

}
