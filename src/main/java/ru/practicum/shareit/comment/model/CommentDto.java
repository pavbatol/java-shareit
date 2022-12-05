package ru.practicum.shareit.comment.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.OnAdd;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link Comment} entity
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
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