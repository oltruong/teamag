package fr.oltruong.teamag.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import fr.oltruong.teamag.ejb.MailEJB;
import fr.oltruong.teamag.ejb.WorkEJB;
import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.utils.CalendarUtils;
import fr.oltruong.teamag.webbean.ColumnDayBean;
import fr.oltruong.teamag.webbean.RealizedFormWebBean;
import fr.oltruong.teamag.webbean.TaskWeekBean;

@SessionScoped
@ManagedBean
public class WorkController
{

    @ManagedProperty( value = "#{loginBean.member}" )
    private Member member;

    private Task newTask = new Task();

    private Map<Task, List<Work>> works;

    private RealizedFormWebBean realizedBean;

    @EJB
    private WorkEJB workEJB;

    @EJB
    private MailEJB mailEJB;

    // FIXME
    private String adminEmail = "TODEFINE";

    public String doCreateActivity()
    {

        System.out.println( "Calling doCreateActivity" );

        if ( StringUtils.isBlank( newTask.getName() ) )
        {
            FacesMessage msg = null;
            msg =
                new FacesMessage( FacesMessage.SEVERITY_ERROR, "Ajout impossible",
                                  "Merci de fournir un nom à la tâche !" );
            FacesContext.getCurrentInstance().addMessage( null, msg );

        }
        else
        {

            try
            {
                workEJB.createTask( realizedBean.getCurrentMonth(), member, newTask );

                works = workEJB.findWorks( member, CalendarUtils.getFirstDayOfMonth( Calendar.getInstance() ) );
                initTaskWeek();

                FacesMessage msg = null;
                msg = new FacesMessage( FacesMessage.SEVERITY_INFO, "Tâche créée", "" );
                FacesContext.getCurrentInstance().addMessage( null, msg );

            }
            catch ( ExistingDataException e )
            {
                FacesMessage msg = null;
                msg = new FacesMessage( FacesMessage.SEVERITY_WARN, "Tâche existante", "Aucune modification" );
                FacesContext.getCurrentInstance().addMessage( null, msg );
            }
        }

        return "realized.xhtml";
    }

    public String deleteTask()
    {
        System.out.println( "Click deleteTask " + realizedBean.getSelectedTaskWeek().getTask().getName() );

        workEJB.removeTask( realizedBean.getSelectedTaskWeek().getTask(), member, realizedBean.getCurrentMonth() );
        init();
        return "realized.xhtml";
    }

    public String previousWeek()
    {
        System.out.println( "Click previous week" );
        realizedBean.decrementWeek();
        initTaskWeek();
        return "realized.xhtml";
    }

    public String nextWeek()
    {
        System.out.println( "Click next week" );
        realizedBean.incrementWeek();
        initTaskWeek();
        return "realized.xhtml";
    }

    public String update()
    {

        System.out.println( "Calling update method" );
        List<Work> changedWorks = findChangedWorks( realizedBean.getTaskWeeks() );
        workEJB.updateWorks( changedWorks );

        FacesMessage msg = null;
        if ( changedWorks.isEmpty() )
        {
            msg = new FacesMessage( FacesMessage.SEVERITY_WARN, "Aucun changement détecté", "" );

        }
        else
        {
            System.out.println( changedWorks.size() + " changements trouvés" );
            msg = new FacesMessage( FacesMessage.SEVERITY_INFO, "Mise à jour effectuée", "" );
            sendNotification();
            initTaskWeek();
        }
        FacesContext.getCurrentInstance().addMessage( null, msg );

        return "realized.xhtml";
    }

    public List<String> completeProject( String query )
    {
        List<Task> tasks = workEJB.findAllTasks();

        List<String> results = new ArrayList<String>( tasks.size() );
        if ( !StringUtils.isBlank( query ) && query.length() > 1 )
        {

            for ( Task task : tasks )
            {
                if ( StringUtils.containsIgnoreCase( task.getProject(), query )
                    && !results.contains( task.getProject() ) )
                {
                    results.add( task.getProject() );
                }

            }
        }
        return results;
    }

    public List<String> completeName( String query )
    {
        List<Task> tasks = workEJB.findAllTasks();

        List<String> results = new ArrayList<String>( tasks.size() );
        if ( !StringUtils.isBlank( query ) && query.length() > 1 )
        {
            for ( Task task : tasks )
            {
                // Do not propose task that the member already has
                if ( !task.getMembers().contains( member ) )
                {
                    if ( StringUtils.containsIgnoreCase( task.getName(), query ) && !results.contains( task.getName() ) )
                    {
                        results.add( task.getName() );
                    }
                }
            }
        }
        return results;
    }

    private void sendNotification()
    {
        int total = workEJB.getSumWorks( member, realizedBean.getCurrentMonth() );

        int nbWorkingDays = CalendarUtils.getWorkingDays( realizedBean.getCurrentMonth() ).size();
        if ( total == nbWorkingDays )
        {
            mailEJB.sendEmail( "TEAMAG", adminEmail, "Réalisé complet pour " + member.getName(), "Il l'a fait" );
        }
    }

    public String init()
    {
        realizedBean = new RealizedFormWebBean();
        realizedBean.setDayCursor( Calendar.getInstance() );
        realizedBean.setCurrentMonth( CalendarUtils.getFirstDayOfMonth( Calendar.getInstance() ) );
        works = workEJB.findWorks( member, CalendarUtils.getFirstDayOfMonth( Calendar.getInstance() ) );

        initTaskWeek();
        return "realized.xhtml";
    }

    public String logout()
    {
        System.out.println( "Logging out" );
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.html";
    }

    private void initTaskWeek()
    {
        if ( works != null )
        {
            Integer weekNumber = realizedBean.getWeekNumber();

            Map<String, ColumnDayBean> mapColumns = new HashMap<String, ColumnDayBean>( 5 );

            List<TaskWeekBean> taskWeekList = new ArrayList<TaskWeekBean>( works.keySet().size() );
            for ( Task task : works.keySet() )
            {
                TaskWeekBean taskWeek = new TaskWeekBean();
                taskWeek.setTask( task );
                for ( Work work : works.get( task ) )
                {

                    if ( work.getDay().get( Calendar.WEEK_OF_YEAR ) == weekNumber )
                    {

                        ColumnDayBean columnDay = new ColumnDayBean();
                        columnDay.setDay( work.getDay() );
                        taskWeek.addWork( columnDay.getDayNumber(), work );

                        if ( mapColumns.get( work.getDayStr() ) == null )
                        {
                            columnDay.addTotal( work.getTotal() );
                            mapColumns.put( work.getDayStr(), columnDay );
                        }
                        else
                        {
                            mapColumns.get( work.getDayStr() ).addTotal( work.getTotal() );
                        }

                    }
                }
                taskWeekList.add( taskWeek );

            }

            realizedBean.getColumnsDay().clear();
            for ( ColumnDayBean col : mapColumns.values() )
            {
                realizedBean.addColumnDay( col );

            }
            Collections.sort( realizedBean.getColumnsDay() );
            realizedBean.setTaskWeeks( taskWeekList );
            Collections.sort( realizedBean.getTaskWeeks() );

        }
        else
        {
            System.out.println( "Aucune taskMonth :'(" );
        }

    }

    private List<Work> findChangedWorks( List<TaskWeekBean> taskWeeks )
    {
        List<Work> worksChanged = new ArrayList<Work>();
        for ( TaskWeekBean taskWeek : taskWeeks )
        {
            for ( Work work : taskWeek.getWorks() )
            {
                if ( work.hasChanged() )
                {
                    worksChanged.add( work );
                }
            }
        }

        return worksChanged;
    }

    // Getters and setters

    public RealizedFormWebBean getRealizedBean()
    {
        return realizedBean;
    }

    public void setRealizedBean( RealizedFormWebBean realizedBean )
    {
        this.realizedBean = realizedBean;
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

}
