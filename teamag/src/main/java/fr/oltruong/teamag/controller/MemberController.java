package fr.oltruong.teamag.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.entity.Member;

@ManagedBean
@RequestScoped
public class MemberController
{

    // ======================================
    // = Attributes =
    // ======================================
    @EJB
    private MemberEJB memberEJB;

    private Member member = new Member();

    private List<Member> memberList = new ArrayList<Member>();

    @PostConstruct
    private void initList()
    {
        memberList = memberEJB.findMembers();
    }

    public String doNewMemberForm()
    {
        return "newMember.xhtml";
    }

    public String doCreateMember()
    {
        member = memberEJB.createMember( member );
        memberList = memberEJB.findMembers();
        return "newMember.xhtml";
    }

    // ======================================
    // = Getters & Setters =
    // ======================================
    public Member getMember()
    {
        return member;
    }

    public void setMember( Member member )
    {
        this.member = member;
    }

    public List<Member> getMemberList()
    {
        return memberList;
    }

    public void setMemberList( List<Member> memberList )
    {
        this.memberList = memberList;
    }

}
