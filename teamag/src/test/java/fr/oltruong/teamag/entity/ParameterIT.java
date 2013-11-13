package fr.oltruong.teamag.entity;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class ParameterIT {

    @Test
    public void testCreation() {
        Parameter parameter = new Parameter();
        parameter.setName(ParameterName.ADMINISTRATOR_EMAIL);
        parameter.setValue("toto");
        // Gets an entity manager and a transaction
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testPersistence");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Persists member to the database
        tx.begin();
        em.persist(parameter);

        tx.commit();

        em.close();
        emf.close();

        assertThat(parameter.getId()).isNotNull();
    }
}
