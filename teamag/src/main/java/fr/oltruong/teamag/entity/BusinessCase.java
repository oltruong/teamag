package fr.oltruong.teamag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table( name = "TM_BUSINESSCASE" )
@Entity
@NamedQuery( name = "findAllBC", query = "SELECT b from BusinessCase b order by b.number" )
public class BusinessCase
{

    @Id
    private Integer number;

    @Column( nullable = false )
    private String name;

    @Column
    private Float amount;

    public Integer getNumber()
    {
        return this.number;
    }

    public void setNumber( Integer number )
    {
        this.number = number;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Float getAmount()
    {
        return this.amount;
    }

    public void setAmount( Float amount )
    {
        this.amount = amount;
    }

}
