package fr.oltruong.teamag.ejb;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.Member;
import fr.oltruong.teamag.entity.WorkDay;

@Stateless
public class MemberEJB
{
    @PersistenceContext( unitName = "ejbPU" )
    private EntityManager em;

    // ======================================
    // = Public Methods =
    // ======================================

    @SuppressWarnings( "unchecked" )
    public List<Member> findMembers()
    {
        Query query = em.createNamedQuery( "findMembers" );
        return query.getResultList();
    }

    public Member findByName( String name )
    {
        Query query = em.createNamedQuery( "findByName" );
        query.setParameter( "fname", name );
        @SuppressWarnings( "unchecked" )
        List<Member> liste = query.getResultList();
        if ( liste == null || liste.isEmpty() )
        {
            return null;
        }
        else
        {
            return liste.get( 0 );
        }
    }

    public Member createMember( Member member )
    {
        em.persist( member );

        // Creation des workdays

        Calendar month = Calendar.getInstance();

        // Génération des activités pour janvier
        month.set( 2013, Calendar.FEBRUARY, 1 );
        for ( int i = 1; i <= 28; i++ )
        {
            Calendar day = (Calendar) month.clone();
            day.set( Calendar.DAY_OF_MONTH, i );

            if ( day.get( Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY
                && day.get( Calendar.DAY_OF_WEEK ) != Calendar.SATURDAY )
            {
                WorkDay workDay = new WorkDay();
                workDay.setDay( day );
                workDay.setMember( member );
                workDay.setMonth( month );

                em.persist( workDay );

            }
        }

        return member;
    }

    public void deleteMember( Member member )
    {
        em.remove( em.merge( member ) );
    }

}
