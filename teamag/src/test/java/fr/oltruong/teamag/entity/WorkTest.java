package fr.oltruong.teamag.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Test;

import fr.oltruong.teamag.utils.CalendarUtils;

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

        em.persist( work.getTask().getMembers().get( 0 ) );
        work.setMember( work.getTask().getMembers().get( 0 ) );
        em.persist( work.getTask() );

        em.persist( work );

        tx.commit();

        em.close();
        emf.close();

        assertNotNull( "Work should have an id", work.getId() );
    }

    @Test
    public void testNamedQuery()
    {

        Work work = createWork();

        // Gets an entity manager and a transaction
        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "testPersistence" );
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists work to the database
        tx.begin();

        em.persist( work.getTask().getMembers().get( 0 ) );
        work.setMember( work.getTask().getMembers().get( 0 ) );
        em.persist( work.getTask() );

        em.persist( work );

        tx.commit();

        Query query = em.createNamedQuery( "findWorksByMember" );
        query.setParameter( "fmemberName", work.getMember().getName() );
        query.setParameter( "fmonth", CalendarUtils.getFirstDayOfMonth( Calendar.getInstance() ) );

        @SuppressWarnings( "unchecked" )
        List<Work> listWorks = query.getResultList();

        assertNotNull( "list should not be null", listWorks );
        assertFalse( "list should not be empty", listWorks.isEmpty() );

        em.close();
        emf.close();

    }

    private Member createMember()
    {
        Member member = new Member();

        member.setName( "Carot" + Calendar.getInstance().getTimeInMillis() );
        member.setCompany( "my company" );
        member.setEmail( "email@dummy.com" );
        return member;
    }

    private Task createTask()
    {
        Task task = new Task();

        task.setName( "Activity" );
        task.setProject( "my project" );

        task.addMember( createMember() );

        return task;
    }

    private Work createWork()
    {
        Work work = new Work();

        work.setMember( createMember() );
        work.setDay( Calendar.getInstance() );
        work.setTask( createTask() );
        work.setMonth( CalendarUtils.getFirstDayOfMonth( Calendar.getInstance() ) );
        return work;
    }
}
