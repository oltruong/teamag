package fr.oltruong.teamag.ejb;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;

public abstract class AbstractEJB {

    @Inject
    protected EntityManager entityManager;

    // @PersistenceContext(unitName = "ejbPU")
    // protected EntityManager entityManager;

    @Inject
    protected Logger logger;

}
