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


@Table(name = "TM_WEEK_COMMENT")
@Entity
@NamedQueries({@NamedQuery(name = "WeekComment.FIND_BY_MEMBER_WEEK_MONTH_YEAR", query = "SELECT w from WeekComment w  where w.member.id=:fmemberId and w.weekYear=:fweekYear and w.month=:fmonth and w.year=:fyear")})
public class WeekComment implements IModel {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 4000)
    private String comment;

    @ManyToOne
    @JoinColumn(nullable = false, name = "MEMBER_FK")
    private Member member;

    @Column
    private int weekYear;

    @Column(name = "TM_MONTH")
    private int month;

    @Column(name = "TM_YEAR")
    private int year;


    public WeekComment() {
        //An empty constructor is required
    }

    public WeekComment(Member theMember, int theWeekYear, int theMonth, int theYear) {
        member = theMember;
        month = theMonth;
        weekYear = theWeekYear;
        year = theYear;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeekYear() {
        return weekYear;
    }

    public void setWeekYear(int weekYear) {
        this.weekYear = weekYear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
