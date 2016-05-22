package com.oltruong.teamag.model;


import com.oltruong.teamag.model.converter.DateConverter;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "TM_ABSENCE_DAY")
@Entity
@NamedQueries({@NamedQuery(name = "findAbsenceDayByAbsenceId", query = "SELECT a FROM AbsenceDay a where a.absence.id=:fAbsenceId"), @NamedQuery(name = "findAllAbsenceDays", query = "SELECT a FROM AbsenceDay a order by a.week, a.member.name"),
        @NamedQuery(name = "findAbsenceDayByMemberAndMonth", query = "SELECT a FROM AbsenceDay a where (a.month=:fMonth and a.member.id=:fMemberId)")})
public class AbsenceDay implements IModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer month;

    private Integer week;


    @Column(nullable = false)
    // @Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private DateTime day;

    @ManyToOne
    @JoinColumn(nullable = false, name = "MEMBER_FK")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "ABSENCE_FK")
    private Absence absence;


    private Float value = Float.valueOf(1f);

    public AbsenceDay(Absence absenceReference) {
        absence = absenceReference;
    }

    public AbsenceDay() {
    }

    public Long getId() {
        return id;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getWeek() {
        return week;
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
        this.month = day.getMonthOfYear();
        this.week = day.getWeekOfWeekyear();
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Absence getAbsence() {
        return absence;
    }

}
