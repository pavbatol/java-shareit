package ru.practicum.shareit.item.storage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.AbstractInMemoryStorage;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository("inMemoryItemStorage")
public class InMemoryItemStorage extends AbstractInMemoryStorage<Item> implements ItemStorage {

    @Override
    public List<Item> findAllByUserId(Long userId) {
        return container.values().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchByNameOrDescription(String text) {
        return container.values().stream()
                .filter(item -> StringUtils.containsIgnoreCase(item.getName(), text)
                        || StringUtils.containsIgnoreCase(item.getDescription(), text))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}
