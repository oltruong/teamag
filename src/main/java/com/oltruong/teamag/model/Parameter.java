package com.oltruong.teamag.model;

import com.oltruong.teamag.model.enumeration.ParameterName;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "TM_PARAMETER")
@Entity
@NamedQuery(name = "Parameter.FIND_ALL", query = "SELECT p from Parameter p")
public class Parameter implements IModel {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ParameterName name;

    @Column
    private String value;

    public Parameter() {
        //Default constructor required
    }

    public Parameter(ParameterName name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public ParameterName getName() {
        return this.name;
    }

    public void setName(ParameterName name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
