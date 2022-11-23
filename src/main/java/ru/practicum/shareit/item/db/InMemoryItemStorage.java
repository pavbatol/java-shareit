package ru.practicum.shareit.item.db;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

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
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item remove(Long id) {
        return items.remove(id);
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
    public List<Item> findAllByUserId(Long userId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), userId))
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
