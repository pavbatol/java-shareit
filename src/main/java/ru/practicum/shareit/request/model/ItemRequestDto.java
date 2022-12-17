package ru.practicum.shareit.request.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Value
public class ItemRequestDto {

    Long id;

    @NotBlank
    @Size(max = 512)
    String description;

    UserDto requester;

    LocalDateTime created;

    @NonFinal
    @Setter
    Set<ItemDto> items;
}
