package ru.practicum.shareit.item.comment.model;

import lombok.Value;
import ru.practicum.shareit.common.OnAdd;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link Comment} entity
 */
@Value
public class CommentDto {

    @NotNull(groups = OnAdd.class)
    Long id;

    @NotNull(groups = OnAdd.class)
    String text;

    @NotNull(groups = OnAdd.class)
    ItemDto item;

    @NotNull(groups = OnAdd.class)
    UserDto author;
}