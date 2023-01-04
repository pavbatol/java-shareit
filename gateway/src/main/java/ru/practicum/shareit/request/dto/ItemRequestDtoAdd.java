package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemRequestDtoAdd {

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
