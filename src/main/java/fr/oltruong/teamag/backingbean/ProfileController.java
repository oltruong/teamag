package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.interfaces.UserLogin;
import fr.oltruong.teamag.utils.MessageManager;
import fr.oltruong.teamag.utils.TeamagUtils;
import fr.oltruong.teamag.webbean.ProfileWebBean;

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
    private MemberEJB memberEJB;

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
