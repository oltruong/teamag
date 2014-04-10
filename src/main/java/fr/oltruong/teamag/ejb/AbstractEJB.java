package fr.oltruong.teamag.ejb;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class AbstractEJB {

    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Logger getLogger() {
        return logger;
    }

    protected Query createNamedQuery(String name) {
        return entityManager.createNamedQuery(name);
    }


    protected void persist(Object object) {
        entityManager.persist(object);
    }

    protected void merge(Object object) {
        entityManager.merge(object);
    }


}
