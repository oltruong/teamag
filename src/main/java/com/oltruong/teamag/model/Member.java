package com.oltruong.teamag.model;

import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.utils.TeamagConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Objects;

@Table(name = "TM_MEMBER")
@Entity
@NamedQueries({@NamedQuery(name = "Member.FIND_ALL", query = "SELECT m from Member m order by m.name"), @NamedQuery(name = "findActiveMembers", query = "SELECT m from Member m where m.active = true order by m.name"), @NamedQuery(name = "findByNamePassword", query = "SELECT m from Member m where m.name=:fname and m.password=:fpassword")})
public class Member implements Serializable, IModel {

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
    private Double estimatedWorkDays;


    private String comment;

    private String absenceHTMLColor;

    @Transient
    private String newPassword;

    @Column(nullable = false)
    private Boolean active = Boolean.TRUE;

    public boolean isSupervisor() {
        return MemberType.SUPERVISOR.equals(memberType) || isAdministrator();
    }

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

    @Override
    public int hashCode() {
        return (id != null)
                ? this.getClass().hashCode() + id.hashCode()
                : super.hashCode();
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

    public String getPassword() {
        return password;
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

    public Double getEstimatedWorkDays() {
        return estimatedWorkDays;
    }


    public void setEstimatedWorkDays(Double estimatedWorkDays) {
        this.estimatedWorkDays = estimatedWorkDays;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getAbsenceHTMLColor() {
        return absenceHTMLColor;
    }

    public void setAbsenceHTMLColor(String absenceHTMLColor) {
        this.absenceHTMLColor = absenceHTMLColor;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    //FIXME REMOVE with JSF
    public Double getEstimatedWorkMonths() {
        return estimatedWorkDays / TeamagConstants.MONTH_DAYS_RATIO;
    }
}
