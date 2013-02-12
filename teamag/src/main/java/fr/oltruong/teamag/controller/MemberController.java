package fr.oltruong.teamag.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

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
        System.out.println( "Do New Member Form" );
        return "newMember.xhtml";
    }

    public String doCreateMember()
    {
        System.out.println( "Do Create Member" );
        member = memberEJB.createMember( member );
        memberList = memberEJB.findMembers();

        FacesMessage msg = null;
        msg =
            new FacesMessage( FacesMessage.SEVERITY_INFO, "Mise à jour effectuée", "Membre " + member.getName()
                + " créé !" );
        FacesContext.getCurrentInstance().addMessage( null, msg );

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
