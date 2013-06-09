package fr.oltruong.teamag.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.Member;

@Stateless
public class AbsenceEJB
{

    @PersistenceContext( unitName = "ejbPU" )
    private EntityManager em;

    @SuppressWarnings( "unchecked" )
    public List<Absence> findAbsences( Member member )
    {
        if ( member != null )
        {

            System.out.println( "Recherche absence pour member " + member.toString() );
            Query query = em.createNamedQuery( "findAbsencesByMember" );
            query.setParameter( "fmemberName", member.getName() );

            return (List<Absence>) query.getResultList();
        }
        else
        {
            System.out.println( "Member null" );
            return null;
        }
    }

    public void addAbsence( Absence absence )
    {

        System.out.println( "Add absence" );
        em.persist( absence );

    }

}
