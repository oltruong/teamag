package fr.oltruong.teamag.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

public class ActivityTest
{
    @Test
    public void testCreation()
    {
        Activity activity = createActivity();
        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "testPersistence" );
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists activity to the database
        tx.begin();

        em.persist( activity.getMembers().get( 0 ) );
        em.persist( activity );

        tx.commit();

        em.close();
        emf.close();

        assertNotNull( "Activity should have an id", activity.getId() );
    }

    @Test
    public void testNamedQuery()
    {

        Activity activity = createActivity();

        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "testPersistence" );
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists activity to the database
        tx.begin();
        em.persist( activity.getMembers().get( 0 ) );
        em.persist( activity );

        tx.commit();
        @SuppressWarnings( "unchecked" )
        List<Activity> listActivitys = em.createNamedQuery( "findAllActivities" ).getResultList();

        assertNotNull( listActivitys );
        assertFalse( listActivitys.isEmpty() );

        em.close();
        emf.close();

    }

    private Activity createActivity()
    {
        Activity activity = new Activity();

        activity.setName( "Activity" );
        activity.setProject( "my project" );

        Member myMember = new Member();
        myMember.setName( "Bob" );
        myMember.setCompany( "my Company" );
        activity.addMember( myMember );

        return activity;
    }
}
