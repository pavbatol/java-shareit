package ru.practicum.shareit.item.db;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;
import static ru.practicum.shareit.validator.ValidatorManager.validateId;

public class InMemoryItemStorage implements ItemStorage {

    private long lastId = 0;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item add(Item item) {
        item.setId(++lastId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        validateId(this, item, null);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item remove(Long id) {
        Item item = getNonNullObject(this, id);
        items.remove(id);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public boolean contains(Long id) {
        return items.containsKey(id);
    }
}
