package ru.practicum.shareit.common.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.common.Entity;
import ru.practicum.shareit.common.Service;
import ru.practicum.shareit.common.Storage;

import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractService<T extends Entity> implements Service<T> {

    private final Storage<T> storage;

    protected final String entityTypeName = getGenericTypeName();

    protected abstract String getGenericTypeName();

    @Override
    public T add(@NotNull T t) {
        T added = storage.add(t);
        log.debug("Добавлен {}: {}", entityTypeName, added);
        return added;
    }

    @Override
    public T update(@NotNull T t) {
        T updated = storage.update(t);
        log.debug("Обновлен {}: {}", entityTypeName, updated);
        return updated;
    }

    @Override
    public T remove(Long id) {
        T removed = storage.remove(id);
        log.debug("Удален {}: {}", entityTypeName, removed);
        return removed;
    }

    @Override
    public List<T> findAll() {
        List<T> found = storage.findAll();
        log.debug("Текущий размер списка для {}: {}", entityTypeName, found.size());
        return found;
    }

    @Override
    public T findById(Long id) {
        T found = getNonNullObject(storage, id);
        log.debug("Найден {}: {}", entityTypeName, found);
        return found;
    }
}
