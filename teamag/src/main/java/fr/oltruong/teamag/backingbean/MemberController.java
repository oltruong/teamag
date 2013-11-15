package fr.oltruong.teamag.backingbean;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.MemberType;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.List;

@ManagedBean
@RequestScoped
public class MemberController extends Controller {

    @Inject
    private MemberEJB memberEJB;
    @Inject
    private Member member;
    private List<Member> memberList = Lists.newArrayList();

    @PostConstruct
    private void initList() {
        memberList = memberEJB.findMembers();
    }

    public String doNewMemberForm() {
        return "newMember";
    }

    public String doCreateMember() {
        member = memberEJB.createMemberWithAbsenceTask(member);
        memberList = memberEJB.findMembers();

        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("updated"), getMessage("memberCreated", member.getName()));
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        return "newMember";
    }

    public List<String> completeCompany(String query) {

        List<String> results = Lists.newArrayListWithExpectedSize(memberList.size());

        if (!StringUtils.isBlank(query) && query.length() > 1) {

            for (Member member : memberList) {
                if (StringUtils.containsIgnoreCase(member.getCompany(), query) && !results.contains(member.getCompany())) {
                    results.add(member.getCompany());
                }

            }
        }
        return results;

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

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

}
