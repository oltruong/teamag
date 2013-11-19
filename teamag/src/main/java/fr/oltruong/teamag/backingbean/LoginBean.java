package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.exception.UserNotFoundException;
import fr.oltruong.teamag.qualifier.UserLogin;
import fr.oltruong.teamag.utils.TeamagConstants;
import fr.oltruong.teamag.utils.TeamagUtils;
import org.slf4j.Logger;

import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class LoginBean
        extends Controller {

    @Inject
    private Logger logger;
    @Inject
    private MemberEJB memberEJB;
    @Inject
    private HttpServletRequest servletRequest;
    private Member member;
    private String username;
    private String password = "";

    public String getLoggedUserName() {
        if (getMember() == null) {
            return "Not loggedIn";
        } else {
            return getMember().getName();
        }
    }

    public boolean isLoggedIn() {
        return getMember() != null;
    }

    public boolean isNotLoggedIn() {
        return !isLoggedIn();
    }

    public boolean isAdministrator() {
        return getMember() != null && getMember().isAdministrator();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Member getMember() {
        if (member == null) {
            member = getMemberFromSession();
        }
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Produces
    @UserLogin
    public Member getMemberFromSession() {

        if (servletRequest == null || servletRequest.getSession() == null) {
            return null;
        }

        return (Member) servletRequest.getSession().getAttribute(TeamagConstants.USER);

    }

    public String login() {

        logger.info("Login={}", username);

        FacesMessage userMessage = null;
        Member member;
        String passwordHashed = null;
        try {

            passwordHashed = TeamagUtils.hashPassword(password);

            member = memberEJB.findMember(username, passwordHashed);
            logger.info(member.getName() + " found");
            userMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("welcome", username), "");
            setMember(member);
            getMember();
            servletRequest.getSession().setAttribute(TeamagConstants.USER, member);
            FacesContext.getCurrentInstance().addMessage(null, userMessage);
            return "welcome";
        } catch (UserNotFoundException e) {
            logger.warn("login [" + username + "] passwordhashed [" + passwordHashed + "] not found");
            userMessage =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("unknown", username),
                            getMessage("tryagain"));

            FacesContext.getCurrentInstance().addMessage(null, userMessage);
            return "login.xhtml";
        }

    }

    public String logout() {

        logger.info("Logging out");
        setMember(null);
        servletRequest.getSession().setAttribute(TeamagConstants.USER, null);

        FacesMessage userMessage =
                new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("farewell", username), getMessage("seeYou"));

        FacesContext.getCurrentInstance().addMessage(null, userMessage);
        return "welcome.xhtml";
    }

}
