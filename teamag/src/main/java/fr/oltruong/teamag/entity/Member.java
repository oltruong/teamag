package fr.oltruong.teamag.entity;

import fr.oltruong.teamag.utils.TeamagConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Table(name = "TM_MEMBER")
@Entity
@NamedQueries({@NamedQuery(name = "findMembers", query = "SELECT m from Member m order by m.name"), @NamedQuery(name = "findByNamePassword", query = "SELECT m from Member m where m.name=:fname and m.password=:fpassword")})
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private String password;
    @Column(nullable = false)
    private String company;


    @Column(nullable = false)
    private String email;
    @Column
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Column(nullable = false)
    private Float estimatedworkDays;


    private String comment;

    public boolean isAdministrator() {
        return MemberType.ADMINISTRATOR.equals(memberType);
    }

    @Override
    public boolean equals(Object otherMember) {
        if (!(otherMember instanceof Member)) {
            return false;
        }
        Member member0 = (Member) otherMember;
        return Objects.equals(id, member0.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public Float getEstimatedworkDays() {
        return estimatedworkDays;
    }

    public Float getEstimatedworkMonths() {
        return estimatedworkDays / TeamagConstants.MONTH_DAYS_RATIO;
    }

    public void setEstimatedworkDays(Float estimatedworkDays) {
        this.estimatedworkDays = estimatedworkDays;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
