package fr.oltruong.teamag.backingbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.MemberType;

@ManagedBean
@RequestScoped
public class MemberController {

    // ======================================
    // = Attributes =
    // ======================================
    @EJB
    private MemberEJB memberEJB;

    private Member member = new Member();

    private List<Member> memberList = new ArrayList<>();

    @PostConstruct
    private void initList() {
	this.memberList = this.memberEJB.findMembers();
    }

    public String doNewMemberForm() {
	System.out.println("Do New Member Form");
	return "newMember.xhtml";
    }

    public String doCreateMember() {
	System.out.println("Do Create Member");
	this.member = this.memberEJB.createMember(this.member);
	this.memberList = this.memberEJB.findMembers();

	FacesMessage msg = null;
	msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
		"Mise à jour effectuée", "Membre " + this.member.getName()
			+ " créé !");
	FacesContext.getCurrentInstance().addMessage(null, msg);

	return "newMember.xhtml";
    }

    public List<String> completeCompany(String query) {
	List<Member> members = this.memberEJB.findMembers();

	List<String> results = new ArrayList<String>(members.size());

	if (!StringUtils.isBlank(query) && query.length() > 1) {

	    for (Member member : members) {
		if (StringUtils.containsIgnoreCase(member.getCompany(), query)
			&& !results.contains(member.getCompany())) {
		    results.add(member.getCompany());
		}

	    }
	}
	return results;

    }

    // ======================================
    // = Getters & Setters =
    // ======================================
    public Member getMember() {
	return this.member;
    }

    public void setMember(Member member) {
	this.member = member;
    }

    public List<Member> getMemberList() {
	return this.memberList;
    }

    public void setMemberList(List<Member> memberList) {
	this.memberList = memberList;
    }

    public List<String> getMembertypeList() {

	MemberType[] memberTypeArray = MemberType.values();

	List<String> memberTypeList = new ArrayList<String>(
		memberTypeArray.length);
	for (int i = 0; i < memberTypeArray.length; i++) {
	    memberTypeList.add(memberTypeArray[i].toString());
	}
	return memberTypeList;
    }

}
