package com.oltruong.teamag.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "TM_WORK_LOAD")
@Entity
@NamedQueries({@NamedQuery(name = "WorkLoad.FIND_ALL", query = "SELECT w FROM WorkLoad w order by w.businessCase.id, w.member.id"), @NamedQuery(name = "WorkLoad.DELETE_FROM_BUSINESSCASE", query = "DELETE FROM WorkLoad where businessCase.id=:fBusinessCaseId")})
public class WorkLoad implements IModel {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    @JoinColumn(nullable = false, name = "MEMBER_FK")
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false, name = "BC_FK")
    private BusinessCase businessCase;

    private Double estimated = 0d;

    private Double realized = 0d;

    public WorkLoad() {

    }

    public WorkLoad(BusinessCase businessCase, Member member) {
        this.businessCase = businessCase;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }


    public BusinessCase getBusinessCase() {
        return businessCase;
    }


    public Double getEstimated() {
        return estimated;
    }

    public void setEstimated(Double estimated) {
        this.estimated = estimated;
    }

    public Double getRealized() {
        return realized;
    }

    public void setRealized(Double realized) {
        this.realized = realized;
    }


    public void setMember(Member member) {
        this.member = member;
    }

    public void setBusinessCase(BusinessCase businessCase) {
        this.businessCase = businessCase;
    }
}
