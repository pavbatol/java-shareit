package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;
import java.util.Set;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemRequestDto {

    Long id;

    String description;

    UserDto requester;

    LocalDateTime created;

    @NonFinal
    @Setter
    Set<ItemDto> items;
}
