package com.oltruong.teamag.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Olivier Truong
 */
@Table(name = "TM_WORK_REALIZED")
@Entity
@NamedQueries({@NamedQuery(name = "WorkRealized.FIND_ALL", query = "SELECT w FROM WorkRealized w order by w.year, w.month, w.taskId, w.memberId"),
        @NamedQuery(name = "WorkRealized.FIND_BY_MEMBER", query = "SELECT w FROM WorkRealized w where w.memberId=:fMemberId order by w.year, w.month, w.taskId ")})

public class WorkRealized {


    @Id
    @GeneratedValue
    private Long id;

    private Long memberId;

    @Column(nullable = false)
    private Long taskId;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    private Double realized = 0d;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getRealized() {
        return realized;
    }

    public void setRealized(Double realized) {
        this.realized = realized;
    }
}
