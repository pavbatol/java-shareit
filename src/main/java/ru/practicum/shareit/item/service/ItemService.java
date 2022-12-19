package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemDtoResponse;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long itemId, Long userId);

    List<ItemDtoResponse> findAllByUserId(Long userId, int from, int size);

    ItemDtoResponse findById(Long itemId, Long userId);

    List<ItemDto> searchByNameOrDescription(String text, int from, int size);
}
