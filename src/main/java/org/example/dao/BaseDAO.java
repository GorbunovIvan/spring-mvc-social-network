package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BaseDAO<T> implements DAO<T> {

    private EntityManagerFactory entityManagerFactory;

    private final Class<T> tClass;

    public BaseDAO(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public T read(int id) {
        return doInSessionAndReturn(entityManager -> entityManager.find(tClass, id));
    }

    @Override
    public Set<T> readAll() {
        return doInSessionAndReturn(entityManager ->
                entityManager.createQuery("FROM " + tClass.getSimpleName(), tClass)
                        .getResultStream()
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public void create(T entity) {
        doInSession(entityManager -> entityManager.persist(entity));
    }

    @Override
    public void update(T entity) {
        doInSession(entityManager -> entityManager.merge(entity));
    }

    @Override
    public void update(int id, T entity) {
        doInSession(entityManager -> {
//            if (entity.getId() == null)
//                entity.setId(id);
            entityManager.merge(entity);
        });
    }

    @Override
    public void delete(int id) {
        doInSession(entityManager -> {
            T entity = entityManager.find(tClass, id);
            entityManager.remove(entity);
        });
    }

    protected void doInSession(Consumer<EntityManager> consumer) {
        var entityManager = entityManagerFactory.createEntityManager();
        consumer.accept(entityManager);
        entityManager.close();
    }

    protected <R> R doInSessionAndReturn(Function<EntityManager, R> function) {
        var entityManager = entityManagerFactory.createEntityManager();
        R entity = function.apply(entityManager);
        entityManager.close();
        return entity;
    }
}
