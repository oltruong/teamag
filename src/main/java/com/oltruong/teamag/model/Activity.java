package com.oltruong.teamag.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "TM_ACTIVITY")
@Entity
@NamedQueries({@NamedQuery(name = "Activity.FIND_ALL", query = "SELECT a from Activity a order by a.name"),
        @NamedQuery(name = "Activity.FIND_BY_NAME_BC", query = "SELECT a from Activity a where a.name=:fname and a.businessCase=:fbc"),
        @NamedQuery(name = "Activity.REMOVE_BC", query = "UPDATE Activity SET businessCase=NULL where businessCase.id=:fBusinessCaseId")})
public class Activity extends Delegable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "BC_FK")
    private BusinessCase businessCase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BusinessCase getBusinessCase() {
        return businessCase;
    }

    public void setBusinessCase(BusinessCase businessCase) {
        this.businessCase = businessCase;
    }
}


