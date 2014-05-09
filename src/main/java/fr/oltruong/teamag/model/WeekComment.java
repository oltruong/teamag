package fr.oltruong.teamag.model;

import javax.persistence.*;

/**
 * @author Olivier Truong
 */
@Table(name = "TM_WEEK_COMMENT")
@Entity
@NamedQueries({@NamedQuery(name = "findWeekComment", query = "SELECT w from WeekComment w  where w.member.id=:fmemberId and w.weekYear=:fweekYear and w.year=:fyear")})
public class WeekComment {

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

    @Column(name = "TM_YEAR")
    private int year;


    public WeekComment() {
    }

    public WeekComment(Member theMember, int theWeekYear, int theYear) {
        member = theMember;
        weekYear = theWeekYear;
        year = theYear;
    }

    public Long getId() {
        return id;
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
