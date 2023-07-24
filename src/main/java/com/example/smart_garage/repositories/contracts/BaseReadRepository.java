package com.example.smart_garage.repositories.contracts;

import java.util.List;

public interface BaseReadRepository <T> {
    List<T> getAll();

    T getById(long id);

    T getByName(String name);
    <V> T getByField(String name, V value);

}
