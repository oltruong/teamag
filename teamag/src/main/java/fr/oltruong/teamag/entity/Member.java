package fr.oltruong.teamag.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "TM_MEMBER")
@Entity
@NamedQueries({
	@NamedQuery(name = "findMembers", query = "SELECT m from Member m order by m.name"),
	@NamedQuery(name = "findByName", query = "SELECT m from Member m where m.name=:fname") })
public class Member implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Float productivity = 1f;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    public Long getId() {
	return this.id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCompany() {
	return this.company;
    }

    public void setCompany(String company) {
	this.company = company;
    }

    public String getEmail() {
	return this.email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public MemberType getMemberType() {
	return this.memberType;
    }

    public void setMemberType(MemberType memberType) {
	this.memberType = memberType;
    }

    @Override
    public boolean equals(Object otherMember) {
	if (!(otherMember instanceof Member)) {
	    return false;
	}
	Member member0 = (Member) otherMember;
	return this.id.equals(member0.getId());
    }

    public boolean isAdministrator() {
	return MemberType.ADMINISTRATOR.equals(this.memberType);
    }
}
