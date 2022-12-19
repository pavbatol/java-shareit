package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto add(ItemRequestDto dto, Long userId);

    List<ItemRequestDto> findAllByUserId(Long userId);

    ItemRequestDto findById(Long requestId, Long userId);

    List<ItemRequestDto> findAllByPage(Long userId, int from, int size);
}
