package fr.oltruong.teamag.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class WorkIT {
    @Test
    public void testCreation() {
        Work work = createWork();
        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testPersistence");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists work to the database
        tx.begin();

        em.persist(work.getTask().getMembers().get(0));
        work.setMember(work.getTask().getMembers().get(0));
        em.persist(work.getTask());

        em.persist(work);

        tx.commit();

        em.close();
        emf.close();

        assertThat(work.getId()).isNotNull();
    }

    @Test
    public void testNamedQuery() {

        Work work = createWork();

        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testPersistence");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists work to the database
        tx.begin();

        em.persist(work.getTask().getMembers().get(0));
        work.setMember(work.getTask().getMembers().get(0));
        em.persist(work.getTask());

        em.persist(work);

        tx.commit();

        Query query = em.createNamedQuery("findWorksByMember");
        query.setParameter("fmemberName", work.getMember().getName());
        query.setParameter("fmonth", DateTime.now().withDayOfMonth(1));

        @SuppressWarnings("unchecked")
        List<Work> listWorks = query.getResultList();

        assertThat(listWorks).isNotNull().isNotEmpty();

        em.close();
        emf.close();

    }

    private Member createMember() {
        Member member = new Member();

        member.setName("Carot" + Calendar.getInstance().getTimeInMillis());
        member.setCompany("my company");
        member.setEmail("email@dummy.com");
        return member;
    }

    private Task createTask() {
        Task task = new Task();

        task.setName("Activity");
        task.setProject("my project");

        task.addMember(createMember());

        return task;
    }

    private Work createWork() {
        Work work = new Work();

        work.setLogger(LoggerFactory.getLogger(Work.class.getName()));
        work.setMember(createMember());
        work.setDay(DateTime.now());
        work.setTask(createTask());
        work.setMonth(DateTime.now().withDayOfMonth(1));
        return work;
    }
}
