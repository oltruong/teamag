package fr.oltruong.teamag.service;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class AbstractService {

    @Inject
    protected EntityManager entityManager;

    @Inject
    protected Logger logger;

    public Logger getLogger() {
        return logger;
    }

    protected Query createNamedQuery(String name) {
        return entityManager.createNamedQuery(name);
    }

    protected <T> TypedQuery<T> createNamedQuery(String name, Class<T> className) {
        return entityManager.createNamedQuery(name, className);
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
