package com.oltruong.teamag.model;


import com.oltruong.teamag.model.converter.DateConverter;

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
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Table(name = "TM_ABSENCE_DAY")
@Entity
@NamedQueries({@NamedQuery(name = "findAbsenceDayByAbsenceId", query = "SELECT a FROM AbsenceDay a where a.absence.id=:fAbsenceId"), @NamedQuery(name = "findAllAbsenceDays", query = "SELECT a FROM AbsenceDay a order by a.week, a.member.name"),
        @NamedQuery(name = "findAbsenceDayByMemberAndMonth", query = "SELECT a FROM AbsenceDay a where (a.month=:fMonth and a.member.id=:fMemberId)")})
public class AbsenceDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer month;

    private Integer week;


    @Column(nullable = false)
    // @Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private LocalDate day;

    @ManyToOne
    @JoinColumn(nullable = false, name = "MEMBER_FK")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "ABSENCE_FK")
    private Absence absence;


    private Float value = Float.valueOf(1f);

    public AbsenceDay() {

    }

    public AbsenceDay(Absence absenceReference) {
        absence = absenceReference;
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


    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
        this.month = day.getMonthValue();
        this.week = day.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
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
