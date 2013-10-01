package fr.oltruong.teamag.ejb;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import ch.qos.logback.classic.Logger;

public abstract class AbstractEJB {

    @Inject
    protected EntityManager entityManager;

    @Inject
    protected Logger logger;

}
