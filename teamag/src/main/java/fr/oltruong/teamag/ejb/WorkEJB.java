package fr.oltruong.teamag.ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.TaskMonth;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.utils.CalendarUtils;

@Stateless
public class WorkEJB
{

    @PersistenceContext( unitName = "ejbPU" )
    private EntityManager em;

    public List<TaskMonth> findTasksMonth( Member member, Calendar month )
    {

        Query query = em.createNamedQuery( "findTaskMonth" );
        query.setParameter( "fmember", member );
        query.setParameter( "fmonth", CalendarUtils.getFirstDayOfMonth( month ) );

        @SuppressWarnings( "unchecked" )
        List<TaskMonth> listTaskMonth = query.getResultList();

        if ( listTaskMonth == null || listTaskMonth.isEmpty() )
        {
            System.out.println( "Creating a new task month for member " + member.getName() );
            listTaskMonth = createTasksMonth( member, month );
        }

        return listTaskMonth;
    }

    private List<TaskMonth> createTasksMonth( Member member, Calendar month )
    {
        List<TaskMonth> listTaskMonths = null;
        List<Task> tasks = findMemberTasks( member );
        if ( tasks != null && !tasks.isEmpty() )
        {
            listTaskMonths = new ArrayList<TaskMonth>( tasks.size() );

            List<Calendar> workingDays = CalendarUtils.getWorkingDays( month );
            for ( Task task : tasks )
            {
                TaskMonth taskMonth = new TaskMonth();
                taskMonth.setMonth( CalendarUtils.getFirstDayOfMonth( month ) );
                taskMonth.setTask( task );
                taskMonth.setMember( member );

                for ( Calendar day : workingDays )
                {
                    Work work = new Work();
                    work.setDay( day );
                    work.setMember( member );
                    work.setMonth( month );
                    work.setTask( task );
                    em.persist( work );
                    taskMonth.addWork( work );
                    System.out.println( "Creation Work pour tache " + task.getName() + " jour " + work.getDayStr() );
                }
                em.persist( taskMonth );
                System.out.println( "Creation taskMonth pour tache " + task.getName() );
                listTaskMonths.add( taskMonth );
            }

        }
        else
        {
            System.out.println( "Aucune activite" );
        }
        return listTaskMonths;

    }

    public List<Task> findMemberTasks( Member member )
    {
        Query query = em.createNamedQuery( "findAllTasks" );

        @SuppressWarnings( "unchecked" )
        List<Task> allTasks = query.getResultList();

        List<Task> tasks = new ArrayList<Task>();

        for ( Task task : allTasks )
        {
            System.out.println( "tache " + task.getId() );

            System.out.println( "tache Name" + task.getMembers().get( 0 ).getName() );
            System.out.println( "tache Id" + task.getMembers().get( 0 ).getId() );
            System.out.println( "Member id" + member.getId() );

            boolean trouve = false;
            for ( Member memberT : task.getMembers() )
            {
                if ( memberT.getId().equals( member.getId() ) )
                {
                    trouve = true;
                }
            }
            if ( trouve )
            {
                System.out.println( "youpi" );
                tasks.add( task );
            }
        }

        return tasks;
    }

    public Task createTask( Task task )
    {
        em.persist( task );
        return task;
    }

}
