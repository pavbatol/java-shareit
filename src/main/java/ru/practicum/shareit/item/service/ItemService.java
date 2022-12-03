package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long itemId, Long userId);

    List<ItemDto> findAllByUserId(Long userId);

    ItemDto findById(Long itemId);

    List<ItemDto> searchByNameOrDescription(String text);
}
