package com.oltruong.teamag.service;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.*;
import java.util.List;

public abstract class AbstractService<E extends Object> {



    @Inject
    protected EntityManager entityManager;

    @Inject
    protected Logger logger;

    abstract Class<E> entityProvider();

    public abstract List<E> findAll();

    public E find(Long id) {
        return entityManager.find(entityProvider(), id);
    }

    public E persist(E entity) {
        entityManager.persist(entity);
        return entity;
    }


    public void merge(E object) {
        entityManager.merge(object);
    }

    public void remove(Long id) {
        E entity = find(id);
        if (entity == null) {
            throw new EntityNotFoundException("Entity with id " + id + " not found");
        }
        remove(entity);
    }

    protected void remove(E entity) {
        entityManager.remove(entity);
    }

    protected E find(Class<E> className, Long id) {
        return entityManager.find(className, id);
    }

    protected <T> T findOtherEntity(Class<T> className, Long id) {
        return entityManager.find(className, id);
    }

    protected void remove(Class<E> className, Long id) {

        E entity = find(className, id);
        remove(entity);
    }


    protected <E> TypedQuery<E> createTypedQuery(String name) {
        return entityManager.createNamedQuery(name, (Class<E>) entityProvider());
    }

    protected <E> TypedQuery<E> createTypedQuery(String name, Class<E> className) {
        return entityManager.createNamedQuery(name, className);
    }

    protected List<E> getTypedQueryList(String name) {
        return createTypedQuery(name, entityProvider()).getResultList();
    }


    protected Query createNamedQuery(String query) {
        return entityManager.createNamedQuery(query);
    }

    protected void flush() {
        entityManager.flush();
    }


}
