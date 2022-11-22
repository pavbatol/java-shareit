package ru.practicum.shareit.common;

import java.util.List;
import java.util.Optional;

public interface CrudStorage<T> {
    T add(T t);

    T update(T t);

    T remove(Long id);

    Optional<T> findById(Long id);

    List<T> findAll();

    boolean contains(Long id);
}
