package ru.practicum.shareit.common;

import java.lang.reflect.Field;
import java.util.*;

public abstract class AbstractInMemoryCrudStorage<T> implements CrudStorage<T> {

    private static final String ID_FIELD_NAME = "id";
    private long lastId = 0;
    protected final Map<Long, T> container = new HashMap<>();

    private long generateId() {
        return ++lastId;
    }

    @Override
    public T add(T t) {
        long id = generateId();
        setObjId(t, id);
        container.put(id, t);
        return t;
    }

    @Override
    public T update(T t) {
        long id = getObjId(t);
        container.put(id, t);
        return t;
    }

    @Override
    public T remove(Long id) {
        return container.remove(id);
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(container.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(container.values());
    }

    protected void setObjId(T t, long id) {
        try {
            Field idField = t.getClass().getDeclaredField(ID_FIELD_NAME);
            idField.setAccessible(true);
            idField.set(t, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("It is not possible to change the 'id' field", e);
        }
    }

    protected long getObjId(T t) {
        long id;
        try {
            Field idField = t.getClass().getDeclaredField(ID_FIELD_NAME);
            idField.setAccessible(true);
            id = (Long) idField.get(t);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("It is not possible to change the 'id' field", e);
        }
        return id;
    }
}
