package com.example.smart_garage.repositories;

import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.repositories.contracts.BaseReadRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class AbstractReadRepository<T> implements BaseReadRepository<T> {
    private final Class<T> clazz;
    protected SessionFactory sessionFactory;

    public AbstractReadRepository(Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(String.format("from %s", clazz.getName()), clazz).list();
        }
    }
    @Override
    public T getById(long id) {
        return getByField("id", id);
    }
    @Override
    public T getByName(String name) {
        return getByField("name", name);
    }

    @Override
    public <V> T getByField(String name, V value) {
        String query = String.format("from %s where %s = :value", clazz.getName(), name);
        String notFoundErrorMessage = String.format("%s with %s %s not found", clazz.getSimpleName(), name, value);

        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery(query, clazz)
                    .setParameter("value", value)
                    .uniqueResultOptional()
                    .orElseThrow(() -> new EntityNotFoundException(notFoundErrorMessage));
        }
    }
}