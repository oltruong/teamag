package fr.oltruong.teamag.producer;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PersistenceContextProducer {

    @Produces
    @PersistenceContext(unitName = "ejbPU")
    private EntityManager entityManager;
}
