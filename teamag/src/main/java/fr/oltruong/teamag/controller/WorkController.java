package fr.oltruong.teamag.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import fr.oltruong.teamag.ejb.WorkEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.TaskMonth;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.entity.WorkDay;

@ManagedBean
@RequestScoped
public class WorkController
{

    @ManagedProperty( value = "#{loginBean.member}" )
    private Member member;

    private Task newActivity = new Task();

    private List<WorkDay> workDayList = new ArrayList<WorkDay>();

    private List<TaskMonth> taskMonthList = new ArrayList<TaskMonth>();

    @EJB
    private WorkEJB workEJB;

    public String doRealizedForm()
    {

        if ( false && member == null )
        {
            return "index.html";
        }

        return "realized.xhtml";
    }

    @PostConstruct
    private void buildDummy()
    {

        Task task = new Task();

        task.setName( "TaskTest" );

        TaskMonth taskMonth = new TaskMonth();

        taskMonth.setTask( task );

        Calendar month = Calendar.getInstance();

        // Génération des activités pour janvier
        month.set( 2013, Calendar.FEBRUARY, 1 );

        taskMonth.setMonth( month );
        for ( int i = 1; i <= 28; i++ )
        {
            Calendar day = (Calendar) month.clone();
            day.set( Calendar.DAY_OF_MONTH, i );

            if ( day.get( Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY
                && day.get( Calendar.DAY_OF_WEEK ) != Calendar.SATURDAY )
            {
                Work work = new Work();
                work.setDay( day );
                work.setMember( member );

                work.setActivity( task );

                System.out.println( "coucou" );

                taskMonth.addWork( work );

            }
        }

        System.out.println( "yeeeah" );
        taskMonthList.add( taskMonth );
        taskMonthList.add( taskMonth );
    }

    public String doCreateActivity()
    {
        newActivity.addMember( member );
        newActivity = workEJB.createActivity( newActivity );
        workDayList = workEJB.getWorkDayList( member );
        return "realized.xhtml";
    }

    public String update()
    {

        System.out.println( "cool " + taskMonthList.get( 0 ).getTask().getName() );
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

    public Task getNewActivity()
    {
        return newActivity;
    }

    public void setNewActivity( Task newActivity )
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

    public List<TaskMonth> getTaskMonthList()
    {
        return taskMonthList;
    }

    public void setTaskMonthList( List<TaskMonth> taskMonthList )
    {
        this.taskMonthList = taskMonthList;
    }

}
