package fr.oltruong.teamag.entity;


import fr.oltruong.teamag.entity.converter.DateConverter;
import org.joda.time.DateTime;

import javax.persistence.*;

@Table(name = "TM_ABSENCE_DAY")
@Entity
public class AbsenceDay {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)

    private Integer month;

    private Integer week;


    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private DateTime day;

    @ManyToOne
    @JoinColumn(nullable = false, name = "MEMBER_FK")
    private Member member;

    private Float value = Float.valueOf(1f);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
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
}
