package fr.oltruong.teamag.ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.utils.CalendarUtils;

@Stateless
public class WorkEJB
{

    @PersistenceContext( unitName = "ejbPU" )
    private EntityManager em;

    public Map<Task, List<Work>> findWorks( Member member, Calendar month )
    {

        Map<Task, List<Work>> worksByTask = new HashMap<Task, List<Work>>();

        Query query = em.createNamedQuery( "findWorksByMember" );
        query.setParameter( "fmemberName", member.getName() );
        query.setParameter( "fmonth", month );

        @SuppressWarnings( "unchecked" )
        List<Work> listWorks = query.getResultList();

        if ( listWorks == null || listWorks.isEmpty() )
        {
            System.out.println( "Creating new works for member " + member.getName() );
            listWorks = createWorks( member, month );
        }

        worksByTask = transformWorkList( listWorks );

        return worksByTask;
    }

    private Map<Task, List<Work>> transformWorkList( List<Work> listWorks )
    {

        Map<Task, List<Work>> worksByTask = new HashMap<Task, List<Work>>();

        for ( Work work : listWorks )
        {
            if ( !worksByTask.containsKey( work.getTask() ) )
            {
                worksByTask.put( work.getTask(), new ArrayList<Work>() );
            }
            worksByTask.get( work.getTask() ).add( work );
        }

        return worksByTask;
    }

    private List<Work> createWorks( Member member, Calendar month )
    {

        List<Work> works = null;

        List<Task> tasks = findMemberTasks( member );
        if ( tasks != null && !tasks.isEmpty() )
        {

            List<Calendar> workingDays = CalendarUtils.getWorkingDays( month );

            works = new ArrayList<Work>( tasks.size() * workingDays.size() );
            for ( Task task : tasks )
            {
                for ( Calendar day : workingDays )
                {
                    Work work = new Work();
                    work.setDay( day );
                    work.setMember( member );
                    work.setMonth( month );
                    work.setTask( task );

                    em.persist( work );

                    works.add( work );
                }
            }

        }
        else
        {
            System.out.println( "Aucune activite" );
        }
        return works;

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

            if ( task.getMembers().contains( member ) )
            {
                System.out.println( "la tâche a bien comme member " + member.getName() );
                tasks.add( task );
            }
        }

        return tasks;
    }

    public void createTask( Calendar month, Member member, Task task )
        throws ExistingDataException
    {
        Query query = em.createNamedQuery( "findTaskByName" );
        query.setParameter( "fname", task.getName() );
        query.setParameter( "fproject", task.getProject() );

        Task taskDB = null;
        @SuppressWarnings( "unchecked" )
        List<Task> allTasks = query.getResultList();

        if ( allTasks != null && !allTasks.isEmpty() )
        {
            System.out.println( "La tâche existe déjà" );
            // La tâche existe déjà
            Task myTask = allTasks.get( 0 );
            if ( myTask.getMembers().contains( member ) )
            {
                System.out.println( "Déjà affectée à la personne" );
                throw new ExistingDataException();
            }
            else
            {
                System.out.println( "Affectation à la personne " + member.getId() );
                myTask.addMember( member );
                em.merge( myTask );
                taskDB = myTask;
            }
        }
        else
        // Création de la tâche
        {
            System.out.println( "Création d'une nouvelle tâche" );

            // Reset task ID
            task.setId( null );
            task.addMember( member );
            em.persist( task );
            taskDB = task;

        }

        em.flush();

        // Création des objets Work
        System.out.println( "Création des objets WORK" );
        List<Calendar> workingDays = CalendarUtils.getWorkingDays( month );

        for ( Calendar day : workingDays )
        {
            Work work = new Work();
            work.setDay( day );
            work.setMember( member );
            work.setMonth( month );
            work.setTask( taskDB );

            em.persist( work );
        }

    }

    public void updateWorks( List<Work> works )
    {
        for ( Work work : works )
        {
            work.setTotal( work.getTotalEdit() );
            em.merge( work );
        }
    }

}
