package fr.oltruong.teamag.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import fr.oltruong.teamag.ejb.ActivityEJB;
import fr.oltruong.teamag.entity.BusinessCase;

@ManagedBean
@RequestScoped
public class BCController
{

    // ======================================
    // = Attributes =
    // ======================================
    @EJB
    private ActivityEJB activityEJB;

    private BusinessCase bc = new BusinessCase();

    private List<BusinessCase> bcList = new ArrayList<BusinessCase>();

    @PostConstruct
    private void initList()
    {
        bcList = activityEJB.findBC();
    }

    public String doCreateBC()
    {
        System.out.println( "Do Create BC" );
        bc = activityEJB.createBC( bc );
        bcList = activityEJB.findBC();

        FacesMessage msg = null;
        msg =
            new FacesMessage( FacesMessage.SEVERITY_INFO, "Mise à jour effectuée", "BC " + bc.getNumber().toString()
                + " " + bc.getName() + " créé !" );
        FacesContext.getCurrentInstance().addMessage( null, msg );

        return "newMember.xhtml";
    }

    // ======================================
    // = Getters & Setters =
    // ======================================
    public BusinessCase getBc()
    {
        return bc;
    }

    public void setBc( BusinessCase bc )
    {
        this.bc = bc;
    }

    public List<BusinessCase> getBcList()
    {
        return bcList;
    }

    public void setBcList( List<BusinessCase> bcList )
    {
        this.bcList = bcList;
    }

}
