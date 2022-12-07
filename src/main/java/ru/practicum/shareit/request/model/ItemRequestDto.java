package ru.practicum.shareit.request.model;

import lombok.Value;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Value
public class ItemRequestDto {

    Long id;

    @NotBlank
    @Size(max = 50)
    String name;

    @NotBlank
    @Size(max = 200)
    String description;

    @NotNull
    UserDto requester;

    @NotNull
    LocalDateTime created;
}
