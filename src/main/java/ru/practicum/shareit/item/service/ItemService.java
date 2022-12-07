package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemBookingDto;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long itemId, Long userId);

    List<ItemBookingDto> findAllByUserId(Long userId);

    ItemBookingDto findById(Long itemId, Long userId);

    List<ItemDto> searchByNameOrDescription(String text);
}
