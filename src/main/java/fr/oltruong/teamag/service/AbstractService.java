package fr.oltruong.teamag.service;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public abstract class AbstractService {

    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    protected Query createNamedQuery(String name) {
        return entityManager.createNamedQuery(name);
    }

    protected List getNamedQueryList(String name) {
        return entityManager.createNamedQuery(name).getResultList();
    }


    protected void persist(Object object) {
        entityManager.persist(object);
    }

    protected void merge(Object object) {
        entityManager.merge(object);
    }


    protected void remove(Class className, Long id) {
        remove(find(className, id));
    }

    protected void remove(Object object) {
        entityManager.remove(object);
    }

    protected <T extends Object> T find(Class<T> className, Long id) {
        return entityManager.find(className, id);
    }

    protected void flush() {
        entityManager.flush();
    }


}
