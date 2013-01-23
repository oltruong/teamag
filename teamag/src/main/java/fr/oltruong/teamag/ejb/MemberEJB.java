package fr.oltruong.teamag.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.oltruong.teamag.entity.Member;

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
        return member;
    }

    public void deleteMember( Member member )
    {
        em.remove( em.merge( member ) );
    }

}
