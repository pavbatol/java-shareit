package ru.practicum.shareit.common;

import java.util.List;

public interface Service<T> {

    T add(T t);

    T update(T t);

    T remove(Long id);

    List<T> findAll();

    T findById(Long id);
}
