package ru.practicum.shareit.item.comment.service;

import ru.practicum.shareit.item.comment.model.CommentDto;
import ru.practicum.shareit.item.comment.model.CommentDtoShort;

public interface CommentService {
    CommentDtoShort add(CommentDto commentDto, Long itemId, Long userId);
}
