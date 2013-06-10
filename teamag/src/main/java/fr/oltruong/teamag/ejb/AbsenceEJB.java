package fr.oltruong.teamag.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.Member;

@Stateless
public class AbsenceEJB
    extends AbstractEJB
{

    @SuppressWarnings( "unchecked" )
    public List<Absence> findAbsences( Member member )
    {
        if ( member != null )
        {

            System.out.println( "Recherche absence pour member " + member.toString() );
            Query query = entityManager.createNamedQuery( "findAbsencesByMember" );
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
        entityManager.persist( absence );

    }

}
