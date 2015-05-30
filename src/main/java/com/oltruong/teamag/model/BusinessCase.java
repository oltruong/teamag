package com.oltruong.teamag.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "TM_BUSINESSCASE")
@Entity

@NamedQueries({@NamedQuery(name = "findAllBC", query = "SELECT b from BusinessCase b order by b.identifier"), @NamedQuery(name = "findBCByNumber", query = "SELECT b FROM BusinessCase b WHERE b.identifier=:fidentifier")})

public class BusinessCase implements IModel {


    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String identifier;

    @Column(nullable = false)
    private String name;

    @Column
    private String comment;

    @Column
    private Double amount = 0d;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return this.amount;
    }


    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return this.identifier + "-" + this.name;
    }
}
