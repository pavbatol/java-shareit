package ru.practicum.shareit.item.db;

import ru.practicum.shareit.common.CrudStorage;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage extends CrudStorage<Item> {

    List<Item> findAllByUserId(Long userId);

    List<Item> searchByNameOrDescription(String text);
}
