package fr.oltruong.teamag.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.time.Month;

/**
 * @author Olivier Truong
 */
@Table(name = "TM_WORK_REALIZED")
@Entity
@NamedQueries({@NamedQuery(name = "findAllWorkRealized", query = "SELECT w FROM WorkRealized w order by w.year, w.month, w.task, w.member"),
        @NamedQuery(name = "findAllWorkRealizedByMember", query = "SELECT w FROM WorkRealized w where w.member.id=:fMemberId order by w.year, w.month, w.task ")})

public class WorkRealized {


    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_FK")
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false, name = "TASK_FK")
    private Task task;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Month month;

    private Double realized = 0d;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Double getRealized() {
        return realized;
    }

    public void setRealized(Double realized) {
        this.realized = realized;
    }
}
