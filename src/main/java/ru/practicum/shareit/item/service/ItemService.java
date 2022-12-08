package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemResponseDto;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long itemId, Long userId);

    List<ItemResponseDto> findAllByUserId(Long userId);

    ItemResponseDto findById(Long itemId, Long userId);

    List<ItemDto> searchByNameOrDescription(String text);
}
