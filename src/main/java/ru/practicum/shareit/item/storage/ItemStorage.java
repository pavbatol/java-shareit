package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.common.Storage;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage extends Storage<Item> {

    List<Item> findAllByUserId(Long userId);

    List<Item> searchByNameOrDescription(String text);
}
