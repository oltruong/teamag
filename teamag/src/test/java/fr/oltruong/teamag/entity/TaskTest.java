package fr.oltruong.teamag.entity;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Calendar;
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

        assertThat( task.getId() ).isNotNull();

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

        assertThat( listTasks ).isNotNull().isNotEmpty();

        assertThat( listTasks.get( 0 ).getMembers() ).isNotNull().isNotEmpty();

        em.close();
        emf.close();

    }

    @Test
    public void testNamedQueryFindByName()
    {

        String name = "myName" + Calendar.getInstance().getTimeInMillis();

        Task task1 = createTask();
        Task task2 = createTask();

        task2.setName( name );
        task1.setMembers( null );
        task2.setMembers( null );

        task2.setProject( "" );

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
        query.setParameter( "fproject", "" );

        @SuppressWarnings( "unchecked" )
        List<Task> listTasks = query.getResultList();

        assertThat( listTasks ).isNotNull().isNotEmpty().hasSize( 1 ).contains( task2 );

        Query query2 = em.createNamedQuery( "findTaskByName" );
        query2.setParameter( "fname", task1.getName() );
        query2.setParameter( "fproject", task1.getProject() );

        @SuppressWarnings( "unchecked" )
        List<Task> listTasks2 = query2.getResultList();

        assertThat( listTasks2 ).isNotNull().isNotEmpty().hasSize( 1 ).contains( task1 );

        em.close();
        emf.close();

    }

    private Task createTask()
    {
        Task task = new Task();

        task.setName( "createTask" + Calendar.getInstance().getTimeInMillis() );
        task.setProject( "my project" );

        Member myMember = new Member();
        myMember.setName( "Bob" + Calendar.getInstance().getTimeInMillis() );
        myMember.setCompany( "my Company" );
        myMember.setEmail( "email@dummy.com" );
        task.addMember( myMember );

        return task;
    }
}
