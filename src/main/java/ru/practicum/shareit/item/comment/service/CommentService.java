package ru.practicum.shareit.item.comment.service;

import ru.practicum.shareit.item.comment.model.CommentDto;
import ru.practicum.shareit.item.comment.model.CommentShortDto;

public interface CommentService {
    CommentShortDto add(CommentDto commentDto, Long itemId, Long userId);
}
