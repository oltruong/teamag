package com.oltruong.teamag.service;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class AbstractService<Entity> {


    abstract Class<Entity> entityProvider();

    @Inject
    protected EntityManager entityManager;

    @Inject
    protected Logger logger;

    public abstract List<Entity> findAll();

    public Entity find(Long id) {
        return entityManager.find(entityProvider(), id);
    }

    public Entity persist(Entity entity) {
        entityManager.persist(entity);
        return entity;
    }


    public void merge(Entity object) {
        entityManager.merge(object);
    }

    public void remove(Long id) {
        Entity entity = find(id);
        if (entity == null) {
            throw new EntityNotFoundException("Entity with id " + id + " not found");
        }
        remove(entity);
    }

    protected void remove(Entity entity) {
        entityManager.remove(entity);
    }

    protected Entity find(Class<Entity> className, Long id) {
        return entityManager.find(className, id);
    }

    protected <T> T findOtherEntity(Class<T> className, Long id) {
        return entityManager.find(className, id);
    }

    protected void remove(Class<Entity> className, Long id) {

        Entity entity = find(className, id);
        remove(entity);
    }


    protected <Entity> TypedQuery<Entity> createTypedQuery(String name) {
        return entityManager.createNamedQuery(name, (Class<Entity>) entityProvider());
    }

    protected <Entity> TypedQuery<Entity> createTypedQuery(String name, Class<Entity> className) {
        return entityManager.createNamedQuery(name, className);
    }

    protected List<Entity> getTypedQueryList(String name) {
        return createTypedQuery(name, entityProvider()).getResultList();
    }


    protected Query createNamedQuery(String query) {
        return entityManager.createNamedQuery(query);
    }

    protected void flush() {
        entityManager.flush();
    }


}
