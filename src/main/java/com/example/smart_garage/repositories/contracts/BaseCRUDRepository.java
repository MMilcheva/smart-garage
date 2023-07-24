package com.example.smart_garage.repositories.contracts;

public interface BaseCRUDRepository <T> extends BaseReadRepository<T> {
    void delete(long id);

    void create(T entity);

    void update(T entity);

}
