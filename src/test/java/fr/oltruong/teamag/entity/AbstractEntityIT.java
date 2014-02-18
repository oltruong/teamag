package fr.oltruong.teamag.entity;

import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author Olivier Truong
 */
public abstract class AbstractEntityIT {

    EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;
    EntityTransaction transaction;

    @Before
    public void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("testPersistence");
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();
    }

    @After
    public void tearDown() {
        entityManager.close();
        entityManagerFactory.close();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public EntityTransaction getTransaction() {
        return transaction;
    }
}
