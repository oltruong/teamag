package com.oltruong.teamag.backingbean;

import com.google.common.collect.Lists;
import com.oltruong.teamag.model.enumeration.MemberType;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.utils.TeamagConstants;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.RowEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.List;

@ManagedBean
@SessionScoped
public class MemberController extends Controller {

    @Inject
    private MemberService memberEJB;
    @Inject
    private Member member;
    private List<Member> memberList = Lists.newArrayList();

    @Inject
    private Member selectedMember;

    private static final String VIEWNAME = "members";

    public String init() {
        memberList = memberEJB.findAll();
        return VIEWNAME;
    }

    public String update() {
        for (Member member : memberList) {
            memberEJB.merge(member);
        }
        return VIEWNAME;
    }

    public String doCreateMember() {
        member = memberEJB.persist(member);
        memberList = memberEJB.findAll();

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("updated"), getMessage("memberCreated", member.getName()));
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        member = new Member();
        return VIEWNAME;
    }


    public List<String> completeCompany(String query) {

        List<String> results = Lists.newArrayListWithExpectedSize(memberList.size());

        if (!StringUtils.isBlank(query) && query.length() > 1) {

            for (Member myMember : memberList) {
                if (StringUtils.containsIgnoreCase(myMember.getCompany(), query) && !results.contains(myMember.getCompany())) {
                    results.add(myMember.getCompany());
                }

            }
        }
        return results;

    }


    public void onEdit(RowEditEvent event) {
        Member memberUpdated = (Member) event.getObject();

        memberEJB.merge(memberUpdated);
        FacesMessage msg = new FacesMessage("Member Edited", memberUpdated.getName());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public Double getTotalEstimatedWorkDays() {
        Double total = 0d;
        for (Member member : memberList) {
            total += member.getEstimatedWorkDays();
        }
        return total;
    }

    public Double getTotalEstimatedWorkMonths() {
        return (getTotalEstimatedWorkDays() / TeamagConstants.MONTH_DAYS_RATIO);
    }


    public void onCancel(RowEditEvent event) {
    }


    public List<String> getMembertypeList() {

        MemberType[] memberTypeArray = MemberType.values();

        List<String> memberTypeList = Lists.newArrayListWithExpectedSize(memberTypeArray.length);
        for (int i = 0; i < memberTypeArray.length; i++) {
            memberTypeList.add(memberTypeArray[i].toString());
        }
        return memberTypeList;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Member getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(Member selectedMember) {
        this.selectedMember = selectedMember;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

}
