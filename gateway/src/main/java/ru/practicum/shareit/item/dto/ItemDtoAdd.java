package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemDtoAdd {

    Long id;

    @NotBlank
    @Size(max = 50)
    String name;

    @NotBlank
    @Size(max = 200)
    String description;

    @NotNull
    Boolean available;

    Long requestId;
}
