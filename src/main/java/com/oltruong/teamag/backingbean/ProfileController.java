package com.oltruong.teamag.backingbean;

import com.oltruong.teamag.interfaces.UserLogin;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.utils.MessageManager;
import com.oltruong.teamag.utils.TeamagUtils;
import com.oltruong.teamag.webbean.ProfileWebBean;
import com.oltruong.teamag.model.Member;

import javax.enterprise.inject.Instance;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

/**
 * @author Olivier Truong
 */
@SessionScoped
@ManagedBean
public class ProfileController extends Controller {

    @Inject
    @UserLogin
    private Instance<Member> memberInstance;

    @Inject
    private MemberService memberEJB;

    private ProfileWebBean profileWebBean;

    private static final String VIEWNAME = "profile";

    public String init() {

        setProfileWebBean(new ProfileWebBean(memberInstance.get()));
        return VIEWNAME;
    }


    public String update() {
        Member member = profileWebBean.getMember();
        member.setAbsenceHTMLColor(profileWebBean.getHtmlColor());
        memberEJB.updateMember(member);

        getMessageManager().displayMessage(MessageManager.INFORMATION, "updated");
        return VIEWNAME;
    }

    public String updatePassword() {
        Member member = profileWebBean.getMember();
        member.setPassword(TeamagUtils.hashPassword(profileWebBean.getPassword()));

        memberEJB.updateMember(member);

        getMessageManager().displayMessage(MessageManager.INFORMATION, "updated");
        return VIEWNAME;
    }

    public ProfileWebBean getProfileWebBean() {
        return profileWebBean;
    }

    public void setProfileWebBean(ProfileWebBean profileWebBean) {
        this.profileWebBean = profileWebBean;
    }
}
