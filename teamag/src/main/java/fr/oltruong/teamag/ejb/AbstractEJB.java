package fr.oltruong.teamag.ejb;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;

import com.google.common.annotations.VisibleForTesting;

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

    @VisibleForTesting
    public void setEntityManager(EntityManager entityManagerInjected) {
        entityManager = entityManagerInjected;
    }

    @VisibleForTesting
    public void setLogger(Logger loggerInjected) {
        logger = loggerInjected;
    }
}
