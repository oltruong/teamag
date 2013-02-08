package fr.oltruong.teamag.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

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

        assertNotNull( "Task should have an id", task.getId() );
    }

    @Test
    public void testNamedQueryFindAllTasks()
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
        @SuppressWarnings( "unchecked" )
        List<Task> listTasks = em.createNamedQuery( "findAllTasks" ).getResultList();

        assertNotNull( listTasks );
        assertFalse( listTasks.isEmpty() );
        assertEquals( "list should only have one element", 1, listTasks.size() );
        assertNotNull( listTasks.get( 0 ).getMembers() );
        assertFalse( listTasks.get( 0 ).getMembers().isEmpty() );
        em.close();
        emf.close();

    }

    @Test
    public void testNamedQueryFindByName()
    {

        String name = "myName";

        Task task1 = createTask();
        Task task2 = createTask();

        task2.setName( name );
        task1.setMembers( null );
        task2.setMembers( null );

        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "testPersistence" );
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists activity to the database
        tx.begin();

        em.persist( task1 );
        em.persist( task2 );
        tx.commit();

        Query query = em.createNamedQuery( "findTaskByName" );
        query.setParameter( "fname", name );

        @SuppressWarnings( "unchecked" )
        List<Task> listTasks = query.getResultList();

        assertNotNull( listTasks );
        assertFalse( "list should not be empty", listTasks.isEmpty() );
        assertEquals( "list should only have one item", 1, listTasks.size() );
        assertEquals( "it should be task2", task2, listTasks.get( 0 ) );

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
        myMember.setEmail( "email@dummy.com" );
        task.addMember( myMember );

        return task;
    }
}
