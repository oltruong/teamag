package fr.oltruong.teamag.controller;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.entity.Member;

@ManagedBean( name = "loginBean" )
@SessionScoped
public class LoginController
{

    @EJB
    private MemberEJB memberEJB;

    private Member member;

    private String username;

    private String password;

    public boolean isLoggedIn()
    {
        return member != null;
    }

    public boolean isJack()
    {
        if ( member != null )
        {
            System.out.println( "isJackkk" + member.getName() );
            System.out.println( member != null && "Jack".equals( member.getName() ) );
        }
        else
        {
            System.out.println( "isJackk  null" );
        }
        return member != null && "Jack".equals( member.getName() );
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String doLoginForm()
    {
        return "login.xhtml";
    }

    public Member getMember()
    {
        return member;
    }

    public void setMember( Member member )
    {
        this.member = member;
    }

    public String login( ActionEvent actionEvent )
    {

        RequestContext context = RequestContext.getCurrentInstance();

        FacesMessage userMessage = null;
        boolean loggedIn = false;

        Member member = memberEJB.findByName( username );

        if ( member != null )
        {
            loggedIn = true;
            userMessage = new FacesMessage( FacesMessage.SEVERITY_INFO, "Bienvenue", username );
            setMember( member );
        }
        else
        {
            loggedIn = false;
            userMessage =
                new FacesMessage( FacesMessage.SEVERITY_WARN, "Erreur",
                                  "Nom inconnu, merci de contacter l'administrateur." );

        }

        FacesContext.getCurrentInstance().addMessage( null, userMessage );
        context.addCallbackParam( "loggedIn", loggedIn );
        return "login.xhtml";

    }

    public String logout()
    {
        System.out.println( "LOGGGGOUT" );
        setMember( null );
        return "login.xhtml";
    }

}
