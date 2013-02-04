package fr.oltruong.teamag.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

public class TaskTest
{
    @Test
    public void testCreation()
    {
        Task task = createTask();
        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "testPersistence" );
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists activity to the database
        tx.begin();

        em.persist( task.getMembers().get( 0 ) );
        em.persist( task );

        tx.commit();

        em.close();
        emf.close();

        assertNotNull( "Activity should have an id", task.getId() );
    }

    @Test
    public void testNamedQuery()
    {

        Task activity = createTask();

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
        List<Task> listActivitys = em.createNamedQuery( "findAllActivities" ).getResultList();

        assertNotNull( listActivitys );
        assertFalse( listActivitys.isEmpty() );

        em.close();
        emf.close();

    }

    private Task createTask()
    {
        Task task = new Task();

        task.setName( "Task" );
        task.setProject( "my project" );

        Member myMember = new Member();
        myMember.setName( "Bob" );
        myMember.setCompany( "my Company" );
        task.addMember( myMember );

        return task;
    }
}
