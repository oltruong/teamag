package fr.oltruong.teamag.entity;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

public class WorkTest
{
    @Test
    public void testCreation()
    {
        Work work = createWork();
        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "testPersistence" );
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists work to the database
        tx.begin();

        em.persist( work.getMember() );
        em.persist( work.getActivity().getMembers().get( 0 ) );
        em.persist( work.getActivity() );

        em.persist( work );

        tx.commit();

        em.close();
        emf.close();

        assertNotNull( "Work should have an id", work.getId() );
    }

    private Member createMember()
    {
        Member member = new Member();

        member.setName( "Carot" );
        member.setCompany( "my company" );

        return member;
    }

    private Activity createActivity()
    {
        Activity activity = new Activity();

        activity.setName( "Activity" );
        activity.setProject( "my project" );

        activity.addMember( createMember() );

        return activity;
    }

    private Work createWork()
    {
        Work work = new Work();

        work.setMember( createMember() );
        work.setDay( Calendar.getInstance() );
        work.setActivity( createActivity() );
        work.setMonth( Calendar.getInstance() );
        return work;
    }
}
