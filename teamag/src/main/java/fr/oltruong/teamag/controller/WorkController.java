package fr.oltruong.teamag.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

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

    private List<TaskMonth> taskMonthList;

    private Calendar currentDay = Calendar.getInstance();

    @EJB
    private WorkEJB workEJB;

    // public String doRealizedForm()
    // {
    //
    // System.out.println( "Calling doRealizedForm" );
    // buildDummy();
    //
    // return "realized.xhtml";
    // }

    public String doCreateActivity()
    {

        System.out.println( "Calling doCreateActivity" );
        buildDummy();

        return "realized.xhtml";
    }

    private void buildDummy()
    {
        taskMonthList = new ArrayList<TaskMonth>();
        Task task = new Task();

        task.setName( "TaskTest" );

        TaskMonth taskMonth = new TaskMonth();

        taskMonth.setTask( task );

        Calendar currentDay = Calendar.getInstance();

        // int weekNumber = week.get( Calendar.DAY_OF_WEEK );

        // week.set( 2013, Calendar.FEBRUARY, 1 );

        taskMonth.setMonth( currentDay );
        for ( int i = 2; i <= 6; i++ )
        {
            Calendar day = (Calendar) currentDay.clone();
            day.set( Calendar.DAY_OF_WEEK, i );

            if ( day.get( Calendar.MONTH ) == currentDay.get( Calendar.MONTH ) )
            {
                Work work = new Work();
                work.setDay( day );
                work.setMember( member );

                work.setActivity( task );

                System.out.println( "Add work local" );
                taskMonth.addWork( work );

            }
        }

        taskMonthList.add( taskMonth );
    }

    public String previousWeek()
    {
        System.out.println( "PREVIOUSSSSSSSSSS" );
        currentDay.add( Calendar.WEEK_OF_YEAR, -1 );
        buildDummy();
        return "realized.xhtml";
    }

    public String doNextWeek()
    {
        currentDay.add( Calendar.WEEK_OF_YEAR, 1 );
        return "realized.xhtml";
    }

    public String update()
    {

        System.out.println( "Calling update method" );
        buildDummy();
        buildDummy();
        FacesMessage msg = null;
        msg =
            new FacesMessage( FacesMessage.SEVERITY_INFO, "Mise à jour effectuée", "Merci " + member.getName() + " !" );
        FacesContext.getCurrentInstance().addMessage( null, msg );
        return "realized.xhtml";
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
