package fr.oltruong.teamag.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.Activity;
import fr.oltruong.teamag.entity.Member;
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

    public List<Activity> findMemberActivities()
    {
        Query query = em.createNamedQuery( "findAllActivities" );

        @SuppressWarnings( "unchecked" )
        List<Activity> allActivities = query.getResultList();

        List<Activity> activities = new ArrayList<Activity>();

        for ( Activity activity : allActivities )
        {
            if ( activity.getMembers().contains( member ) )
            {
                activities.add( activity );
            }
        }

        return activities;
    }

    @SuppressWarnings( "unchecked" )
    public List<WorkDay> getWorkDayList( Member member )
    {
        Query query = em.createNamedQuery( "findWorkDaysByMember" );
        query.setParameter( "fmember", member );

        return (List<WorkDay>) query.getResultList();
    }

    public Activity createActivity( Activity activity )
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

    public void deleteActivity( Activity activity )
    {
        em.remove( em.merge( activity ) );
    }

}
