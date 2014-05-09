package fr.oltruong.teamag.model;

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
@NamedQueries({@NamedQuery(name = "findAllWorkLoad", query = "SELECT w FROM WorkLoad w order by w.businessCase.name, w.member.name")})
public class WorkLoad {

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
}
