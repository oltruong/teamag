package fr.oltruong.teamag.entity;

import fr.oltruong.teamag.entity.converter.DateConverter;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name = "TM_ABSENCE")
@Entity
@NamedQuery(name = "findAbsencesByMember", query = "SELECT a FROM Absence a WHERE a.member.id=:fmemberId order by a.beginDate")
public class Absence {

    public static final Integer ALL_DAY = 0;

    public static final Integer MORNING_ONLY = 1;

    public static final Integer AFTERNOON_ONLY = 2;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private DateTime beginDate;

    @Column(nullable = false)
    private Integer beginType = ALL_DAY;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private DateTime endDate;

    @ManyToOne
    @JoinColumn(nullable = false, name = "MEMBER_FK")
    private Member member;

    @Column(nullable = false)
    private Integer endType = ALL_DAY;

    public Long getId() {
        return id;
    }

    public DateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(DateTime beginDate) {
        this.beginDate = beginDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
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
