package ru.practicum.shareit.common.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.common.Controller;
import ru.practicum.shareit.common.Entity;
import ru.practicum.shareit.common.Service;

import javax.validation.Valid;

import java.util.List;

import static ru.practicum.shareit.validator.ValidatorManager.validateEntity;

@Validated
@RequiredArgsConstructor
public abstract class AbstractController<T extends Entity, S extends Service<T>> implements Controller<T> {

    private final S service;

    @Override
    public T add(@Valid @RequestBody T t) {
        validateEntity(t);
        return service.add(t);
    }

    @Override
    public T update(@Valid @RequestBody T t) {
        validateEntity(t);
        return service.update(t);
    }

    @Override
    public List<T> findAll() {
        return service.findAll();
    }

    @Override
    public T findById(Long id) {
        return service.findById(id);
    }
}
