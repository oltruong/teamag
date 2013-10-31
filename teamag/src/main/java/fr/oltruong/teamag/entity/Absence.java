package fr.oltruong.teamag.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name = "TM_ABSENCE")
@Entity
@NamedQueries({ @NamedQuery(name = "findAbsencesByMember", query = "SELECT a FROM Absence a WHERE a.member.name=:fmemberName order by a.beginDate") })
public class Absence {

    public static final Integer ALL_DAY = 0;

    public static final Integer MORNING_ONLY = 1;

    public static final Integer AFTERNOON_ONLY = 2;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date beginDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    private Integer type = ALL_DAY;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTypeStr() {
        if ((type != null) && !type.equals(ALL_DAY)) {
            if (type.equals(AFTERNOON_ONLY)) {
                return "Apr�s-midi";
            } else if (type.equals(MORNING_ONLY)) {
                return "Matin�e";
            }
        }
        return null;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
