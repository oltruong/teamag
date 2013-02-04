package fr.oltruong.teamag.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.Task;
import fr.oltruong.teamag.entity.Work;
import fr.oltruong.teamag.entity.WorkDay;

@Stateless
public class WorkEJB
{

    @PersistenceContext( unitName = "ejbPU" )
    private EntityManager em;

    private Member member;

    public Member getMember()
    {
        return member;
    }

    public void setMember( Member member )
    {
        this.member = member;
    }

    public List<Task> findMemberActivities()
    {
        Query query = em.createNamedQuery( "findAllTasks" );

        @SuppressWarnings( "unchecked" )
        List<Task> allTasks = query.getResultList();

        List<Task> tasks = new ArrayList<Task>();

        for ( Task task : allTasks )
        {
            if ( task.getMembers().contains( member ) )
            {
                tasks.add( task );
            }
        }

        return tasks;
    }

    @SuppressWarnings( "unchecked" )
    public List<WorkDay> getWorkDayList( Member member )
    {
        Query query = em.createNamedQuery( "findWorkDaysByMember" );
        query.setParameter( "fmember", member );

        return (List<WorkDay>) query.getResultList();
    }

    public Task createActivity( Task activity )
    {
        em.persist( activity );
        for ( Member member : activity.getMembers() )
        {

            List<WorkDay> listWorkDays = getWorkDayList( member );

            if ( listWorkDays != null )
            {
                for ( WorkDay workDay : listWorkDays )
                {
                    Work work = new Work();
                    work.setDay( workDay.getDay() );
                    work.setMember( member );
                    work.setMonth( workDay.getMonth() );
                    work.setActivity( activity );
                    workDay.addWork( work );
                    em.persist( work );
                    em.persist( workDay );
                }
            }

        }

        return activity;
    }

    public void deleteActivity( Task activity )
    {
        em.remove( em.merge( activity ) );
    }

}
