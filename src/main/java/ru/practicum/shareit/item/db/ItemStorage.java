package ru.practicum.shareit.item.db;

import ru.practicum.shareit.common.Storage;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage extends Storage<Item> {

    List<Item> findAllByUserId(Long userId);

    List<Item> search(String text);
}
