package ru.practicum.shareit.common;

import ru.practicum.shareit.exeption.NotFoundException;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.validator.impl.ValidatorManager.getNonNullObject;

public interface CrudStorage<T> {
    T add(T t);

    T update(T t);

    T remove(Long id);

    Optional<T> findById(Long id);

    List<T> findAll();

    default boolean contains(Long id) {
        try {
            getNonNullObject(this, id);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }
}