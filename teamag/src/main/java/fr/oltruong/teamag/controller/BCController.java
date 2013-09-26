package fr.oltruong.teamag.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

    private final Logger logger = Logger.getLogger( getClass().getName() );

    // ======================================
    // = Attributes =
    // ======================================
    @EJB
    private ActivityEJB activityEJB;

    private BusinessCase bc = new BusinessCase();

    private Activity activity = new Activity();

    private List<BusinessCase> bcList = new ArrayList<>();

    private List<Activity> activityList = new ArrayList<>();

    private int tabIndex = 0;

    @PostConstruct
    private void initList()
    {
        this.bcList = this.activityEJB.findBC();
        this.activityList = this.activityEJB.findActivities();
    }

    public String doCreateBC()
    {
        this.tabIndex = 0;
        this.logger.info( "Creation of a business case" );
        FacesMessage msg = null;

        if ( this.bc.getNumber() == null || StringUtils.isBlank( this.bc.getNumber().toString() ) )
        {
            msg =
                new FacesMessage( FacesMessage.SEVERITY_ERROR, "Ajout impossible", "Merci de fournir un numéro de BC" );
        }
        else
        {

            try
            {
                this.activityEJB.createBC( this.bc );

                msg =
                    new FacesMessage( FacesMessage.SEVERITY_INFO, "Mise à jour effectuée", "BC "
                        + this.bc.getNumber().toString() + " " + this.bc.getName() + " créé !" );
                // Réinit BC
                this.bc = new BusinessCase();
            }
            catch ( ExistingDataException e )
            {
                e.printStackTrace();
                msg =
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, "Ajout impossible", "Le BC "
                        + this.bc.getNumber().toString() + " existe déjà" );
            }

            this.bcList = this.activityEJB.findBC();
        }
        FacesContext.getCurrentInstance().addMessage( null, msg );

        return "businesscases.xhtml";
    }

    public String doCreateActivity()
    {
        this.tabIndex = 1;
        this.logger.info( "Creation of an activity" );

        FacesMessage msg = null;

        if ( StringUtils.isBlank( this.activity.getName() ) || this.activity.getBc() == null )
        {
            msg =
                new FacesMessage( FacesMessage.SEVERITY_ERROR, "Ajout impossible", "Merci de fournir un nom et un BC" );
        }
        else
        {

            try
            {
                this.activityEJB.createActivity( this.activity );

                msg = new FacesMessage( FacesMessage.SEVERITY_INFO, "Mise à jour effectuée", "Activité créé !" );
                // Réinit BC
                this.activity = new Activity();
            }
            catch ( ExistingDataException e )
            {
                e.printStackTrace();
                msg = new FacesMessage( FacesMessage.SEVERITY_ERROR, "Ajout impossible", "L'activité existe déjà" );
            }

            this.activityList = this.activityEJB.findActivities();
        }
        FacesContext.getCurrentInstance().addMessage( null, msg );

        return "businesscases.xhtml";
    }

    // ======================================
    // = Getters & Setters =
    // ======================================
    public BusinessCase getBc()
    {
        return this.bc;
    }

    public void setBc( BusinessCase bc )
    {
        this.bc = bc;
    }

    public List<BusinessCase> getBcList()
    {
        return this.bcList;
    }

    public void setBcList( List<BusinessCase> bcList )
    {
        this.bcList = bcList;
    }

    public Activity getActivity()
    {
        return this.activity;
    }

    public void setActivity( Activity activity )
    {
        this.activity = activity;
    }

    public List<Activity> getActivityList()
    {
        return this.activityList;
    }

    public void setActivityList( List<Activity> activityList )
    {
        this.activityList = activityList;
    }

    public int getTabIndex()
    {
        return this.tabIndex;
    }

    public void setTabIndex( int tabIndex )
    {
        this.tabIndex = tabIndex;
    }

}
