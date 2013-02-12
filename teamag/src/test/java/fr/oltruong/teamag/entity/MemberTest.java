package fr.oltruong.teamag.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

import org.junit.Test;

public class MemberTest
{

    @Test
    public void testCreation()
    {
        Member member = createMember();
        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "testPersistence" );
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists member to the database
        tx.begin();
        em.persist( member );

        tx.commit();

        em.close();
        emf.close();

        assertNotNull( "Member should have an id", member.getId() );
    }

    @Test( expected = RollbackException.class )
    public void testException()
    {
        Member member = createMember();
        member.setEmail( null );
        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "testPersistence" );
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists member to the database
        tx.begin();
        em.persist( member );

        tx.commit();
        em.close();
        emf.close();

    }

    @Test
    public void testNamedQuery()
    {

        Member member = createMember();

        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "testPersistence" );
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists member to the database
        tx.begin();
        em.persist( member );

        tx.commit();
        @SuppressWarnings( "unchecked" )
        List<Member> listMembers = em.createNamedQuery( "findMembers" ).getResultList();

        assertNotNull( listMembers );
        assertFalse( listMembers.isEmpty() );

        em.close();
        emf.close();

    }

    private Member createMember()
    {
        Member member = new Member();

        member.setName( "Carot" + Calendar.getInstance().getTimeInMillis() );
        member.setCompany( "my company" );
        member.setEmail( "dummy@email.com" );

        return member;
    }
}
