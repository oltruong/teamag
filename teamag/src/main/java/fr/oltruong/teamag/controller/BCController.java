package fr.oltruong.teamag.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import fr.oltruong.teamag.ejb.ActivityEJB;
import fr.oltruong.teamag.entity.Activity;
import fr.oltruong.teamag.entity.BusinessCase;
import fr.oltruong.teamag.exception.ExistingDataException;

@ManagedBean( name = "bcController" )
@RequestScoped
public class BCController
{

    // ======================================
    // = Attributes =
    // ======================================
    @EJB
    private ActivityEJB activityEJB;

    private BusinessCase bc = new BusinessCase();

    private Activity activity = new Activity();

    private List<BusinessCase> bcList = new ArrayList<BusinessCase>();

    private List<Activity> activityList = new ArrayList<Activity>();

    private int tabIndex = 0;

    @PostConstruct
    private void initList()
    {
        bcList = activityEJB.findBC();
        activityList = activityEJB.findActivities();
    }

    public String doCreateBC()
    {
        tabIndex = 0;
        System.out.println( "Do Create BC" );
        FacesMessage msg = null;

        if ( bc.getNumber() == null || StringUtils.isBlank( bc.getNumber().toString() ) )
        {
            msg =
                new FacesMessage( FacesMessage.SEVERITY_ERROR, "Ajout impossible", "Merci de fournir un numéro de BC" );
        }
        else
        {

            try
            {
                activityEJB.createBC( bc );

                msg =
                    new FacesMessage( FacesMessage.SEVERITY_INFO, "Mise à jour effectuée", "BC "
                        + bc.getNumber().toString() + " " + bc.getName() + " créé !" );
                // Réinit BC
                bc = new BusinessCase();
            }
            catch ( ExistingDataException e )
            {
                e.printStackTrace();
                msg =
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, "Ajout impossible", "Le BC "
                        + bc.getNumber().toString() + " existe déjà" );
            }

            bcList = activityEJB.findBC();
        }
        FacesContext.getCurrentInstance().addMessage( null, msg );

        return "businesscases.xhtml";
    }

    public String doCreateActivity()
    {
        tabIndex = 1;
        System.out.println( "Do Create Activity" );
        FacesMessage msg = null;

        if ( StringUtils.isBlank( activity.getName() ) || activity.getBc() == null )
        {
            msg =
                new FacesMessage( FacesMessage.SEVERITY_ERROR, "Ajout impossible", "Merci de fournir un nom et un BC" );
        }
        else
        {

            try
            {
                activityEJB.createActivity( activity );

                msg = new FacesMessage( FacesMessage.SEVERITY_INFO, "Mise à jour effectuée", "Activité créé !" );
                // Réinit BC
                activity = new Activity();
            }
            catch ( ExistingDataException e )
            {
                e.printStackTrace();
                msg = new FacesMessage( FacesMessage.SEVERITY_ERROR, "Ajout impossible", "L'activité existe déjà" );
            }

            activityList = activityEJB.findActivities();
        }
        FacesContext.getCurrentInstance().addMessage( null, msg );

        return "businesscases.xhtml";
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

    public Activity getActivity()
    {
        return activity;
    }

    public void setActivity( Activity activity )
    {
        this.activity = activity;
    }

    public List<Activity> getActivityList()
    {
        return activityList;
    }

    public void setActivityList( List<Activity> activityList )
    {
        this.activityList = activityList;
    }

    public int getTabIndex()
    {
        System.out.println( "ffff" + tabIndex );
        return tabIndex;
    }

    public void setTabIndex( int tabIndex )
    {
        System.out.println( "seeeeffff" + tabIndex );
        this.tabIndex = tabIndex;
    }

}
