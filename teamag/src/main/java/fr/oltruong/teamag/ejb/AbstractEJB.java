package fr.oltruong.teamag.ejb;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractEJB {
    @PersistenceContext(unitName = "ejbPU")
    protected EntityManager entityManager;

}
