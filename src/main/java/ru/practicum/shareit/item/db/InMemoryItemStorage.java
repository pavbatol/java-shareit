package ru.practicum.shareit.item.db;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;
import static ru.practicum.shareit.validator.ValidatorManager.validateId;

@Repository("inMemoryItemStorage")
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

    @Override
    public List<Item> findAllByUserId(Long userId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwnerId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchByNameOrDescription(String text) {
        final String searched = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(searched)
                        || item.getDescription().toLowerCase().contains(searched))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}
