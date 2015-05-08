package com.oltruong.teamag.model;

import com.oltruong.teamag.model.converter.DateConverter;
import java.time.LocalDate;

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

@Table(name = "TM_ABSENCE")
@Entity
@NamedQueries({@NamedQuery(name = "Absence.FIND_BY_MEMBER", query = "SELECT a FROM Absence a WHERE a.member.id=:fmemberId order by a.beginDate"),
        @NamedQuery(name = "Absence.FIND_ALL", query = "SELECT a FROM Absence a order by a.member.name, a.beginDate")})
public class Absence {

    public static final Integer ALL_DAY = 0;

    public static final Integer MORNING_ONLY = 1;

    public static final Integer AFTERNOON_ONLY = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = DateConverter.class)
    private LocalDate beginDate;

    @Column(nullable = false)
    private Integer beginType = ALL_DAY;

    @Column(nullable = false)
    @Convert(converter = DateConverter.class)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(nullable = false, name = "MEMBER_FK")
    private Member member;

    @Column(nullable = false)
    private Integer endType = ALL_DAY;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Integer getBeginType() {
        return beginType;
    }

    public void setBeginType(Integer type) {
        this.beginType = type;
    }

    public Integer getEndType() {
        return endType;
    }

    public void setEndType(Integer endType) {
        this.endType = endType;
    }
}
