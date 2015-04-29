package com.oltruong.teamag.model;

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

    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;
    protected EntityTransaction transaction;

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

    protected Object createWithoutCommit(Object object) {
        entityManager.persist(object);
        return object;
    }

    protected Object createWithCommit(Object object) {
        object = createWithoutCommit(object);
        transaction.commit();
        return object;
    }


    protected void commit() {
        transaction.commit();
    }

    protected void persist(Object object) {
        entityManager.persist(object);
    }


}
