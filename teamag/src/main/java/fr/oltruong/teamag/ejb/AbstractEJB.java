package fr.oltruong.teamag.ejb;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;

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

}
