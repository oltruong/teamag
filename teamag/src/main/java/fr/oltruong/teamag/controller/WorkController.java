package fr.oltruong.teamag.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import fr.oltruong.teamag.ejb.WorkEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.TaskMonth;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.webbean.TaskWeekBean;

@ManagedBean
@SessionScoped
public class WorkController
{

    @ManagedProperty( value = "#{loginBean.member}" )
    private Member member;

    private Task newTask = new Task();

    private List<TaskMonth> taskMonthList;

    private List<TaskWeekBean> taskWeekList;

    private Calendar currentDay;

    @EJB
    private WorkEJB workEJB;

    public String init()
    {
        currentDay = Calendar.getInstance();
        taskMonthList = workEJB.findTasksMonth( member, Calendar.getInstance() );

        initTaskWeek();
        return "realized.xhtml";
    }

    private void initTaskWeek()
    {
        if ( taskMonthList != null )
        {

            taskWeekList = new ArrayList<TaskWeekBean>( taskMonthList.size() );
            for ( TaskMonth taskMonth : taskMonthList )
            {
                TaskWeekBean taskWeek = new TaskWeekBean();
                taskWeek.setTask( taskMonth.getTask() );
                for ( Work work : taskMonth.getWorks() )
                {
                    System.out.println( "work " + work.getDayStr() );

                    if ( work.getDay().get( Calendar.WEEK_OF_MONTH ) == currentDay.get( Calendar.WEEK_OF_MONTH ) )
                    {
                        System.out.println( "cool" );
                        taskWeek.addWork( work );
                    }
                }
                System.out.println( "adddddd" );
                taskWeekList.add( taskWeek );
            }
        }
        else
        {
            System.out.println( "Aucune taskMonth :'(" );
        }

    }

    public String doCreateActivity()
    {

        System.out.println( "Calling doCreateActivity" );

        return "realized";
    }

    public String previousWeek()
    {
        System.out.println( "Click previous week" );
        // currentDay.add( Calendar.WEEK_OF_YEAR, -1 );
        // initTaskWeek();
        return "realized.xhtml";
    }

    public String nextWeek()
    {
        System.out.println( "Click next week" );
        currentDay.add( Calendar.WEEK_OF_YEAR, 1 );
        initTaskWeek();
        return "realized.xhtml";
    }

    public String update()
    {

        System.out.println( "Calling update method" );
        // FacesMessage msg = null;
        // msg =
        // new FacesMessage( FacesMessage.SEVERITY_INFO, "Mise à jour effectuée", "Merci " + member.getName() + " !" );
        // FacesContext.getCurrentInstance().addMessage( null, msg );
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

    public Task getNewTask()
    {
        return newTask;
    }

    public void setNewTask( Task newActivity )
    {
        this.newTask = newActivity;
    }

    public List<TaskMonth> getTaskMonthList()
    {
        return taskMonthList;
    }

    public void setTaskMonthList( List<TaskMonth> taskMonthList )
    {
        this.taskMonthList = taskMonthList;
    }

    public List<TaskWeekBean> getTaskWeekList()
    {
        System.out.println( "Get  " + taskWeekList.get( 0 ).getWorks().size() );
        return taskWeekList;
    }

    public void setTaskWeekList( List<TaskWeekBean> taskWeekList )
    {
        this.taskWeekList = taskWeekList;
    }

}
