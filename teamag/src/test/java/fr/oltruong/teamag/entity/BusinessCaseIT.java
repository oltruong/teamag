package fr.oltruong.teamag.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

import org.junit.Test;

public class BusinessCaseIT {
    @Test
    public void testCreation() {
        BusinessCase businessCase = createBusinessCase(99);
        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testPersistence");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists member to the database
        tx.begin();
        em.persist(businessCase);

        tx.commit();

        em.close();
        emf.close();

        assertThat(businessCase.getNumber()).isNotNull();
    }

    @Test(expected = RollbackException.class)
    public void testException() {
        BusinessCase businessCase = createBusinessCase(567);
        businessCase.setName(null);

        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testPersistence");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists member to the database
        tx.begin();
        em.persist(businessCase);

        tx.commit();
        em.close();
        emf.close();

    }

    @Test
    public void testNamedQuery() {

        BusinessCase businessCase = createBusinessCase(1234);

        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testPersistence");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists member to the database
        tx.begin();
        em.persist(businessCase);

        tx.commit();
        @SuppressWarnings("unchecked")
        List<BusinessCase> businessCaseList = em.createNamedQuery("findAllBC").getResultList();

        assertThat(businessCaseList).isNotNull().isNotEmpty();

        em.close();
        emf.close();

    }

    private BusinessCase createBusinessCase(int number) {
        BusinessCase businessCase = new BusinessCase();

        businessCase.setName("My BC");
        businessCase.setNumber(Integer.valueOf(number));
        businessCase.setAmount(Float.valueOf(3.5f));

        return businessCase;
    }

}
