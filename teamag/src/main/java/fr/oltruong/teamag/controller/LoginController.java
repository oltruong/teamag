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

    private String username;

    private String password;

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
        System.out.println( "heyyyy" );
        return "login.xhtml";
    }

    public void login( ActionEvent actionEvent )
    {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;

        Member member = memberEJB.findByName( username );

        if ( member != null )
        {
            loggedIn = true;
            msg = new FacesMessage( FacesMessage.SEVERITY_INFO, "Bienvenue", username );
        }
        else
        {
            loggedIn = false;
            msg =
                new FacesMessage( FacesMessage.SEVERITY_WARN, "Erreur",
                                  "Nom inconnu, merci de contacter l'administrateur." );

        }

        FacesContext.getCurrentInstance().addMessage( null, msg );
        context.addCallbackParam( "loggedIn", loggedIn );
    }
}
