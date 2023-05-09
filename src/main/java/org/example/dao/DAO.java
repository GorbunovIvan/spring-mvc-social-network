package org.example.dao;

import java.util.Set;

public interface DAO<T> {
    T read(int id);
    Set<T> readAll();
    void create(T entity);
    void update(T entity);
    void update(int id, T entity);
    void delete(int id);
}
