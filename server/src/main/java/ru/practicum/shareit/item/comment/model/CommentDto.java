package ru.practicum.shareit.item.comment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class CommentDto {

    Long id;

    @NotBlank
    String text;

    ItemDto item;

    UserDto author;

    LocalDateTime created;
}