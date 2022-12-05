package ru.practicum.shareit.item.comment.model;

import ru.practicum.shareit.common.Mapper;

@org.mapstruct.Mapper(componentModel = "spring")
public interface commentMapper extends Mapper<Comment, CommentDto> {
}
