package fr.oltruong.teamag.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import fr.oltruong.teamag.ejb.WorkEJB;
import fr.oltruong.teamag.entity.Activity;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.WorkDay;

@ManagedBean
@RequestScoped
public class WorkController
{

    @ManagedProperty( value = "#{loginBean.member}" )
    private Member member;

    private Activity newActivity = new Activity();

    private List<WorkDay> workDayList = new ArrayList<WorkDay>();

    @EJB
    private WorkEJB workEJB;

    public String doRealizedForm()
    {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage( null, new FacesMessage( FacesMessage.SEVERITY_INFO, "Bienvenue", member.getName() ) );

        return "realized.xhtml";
    }

    public String doCreateActivity()
    {
        newActivity.addMember( member );
        newActivity = workEJB.createActivity( newActivity );
        workDayList = workEJB.getWorkDayList( member );
        return "realized.xhtml";
    }

    public void onEdit( RowEditEvent event )
    {
        System.out.println( "ON EDITTTTTTTTTTTTTT" );
        FacesMessage msg = new FacesMessage( "Car Edited", "yahoo" );

        FacesContext.getCurrentInstance().addMessage( null, msg );
    }

    public void onCancel( RowEditEvent event )
    {
        System.out.println( "ON CANCELLED" );
        FacesMessage msg = new FacesMessage( "Car Cancelled", "yahoo" );

        FacesContext.getCurrentInstance().addMessage( null, msg );
    }

    public Member getMember()
    {
        return member;
    }

    public void setMember( Member member )
    {
        this.member = member;
    }

    public Activity getNewActivity()
    {
        return newActivity;
    }

    public void setNewActivity( Activity newActivity )
    {
        this.newActivity = newActivity;
    }

    public List<WorkDay> getWorkDayList()
    {
        return workDayList;
    }

    public void setWorkDayList( List<WorkDay> workDayList )
    {
        this.workDayList = workDayList;
    }

}
