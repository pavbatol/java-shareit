package ru.practicum.shareit.item.model;

import lombok.Value;
import ru.practicum.shareit.common.OnAdd;
import ru.practicum.shareit.validator.annotated.NullOrNotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Value
public class ItemDto {

    Long id;

    @NotNull(groups = OnAdd.class)
    @NullOrNotBlank
    @Size(max = 50)
    String name;

    @NotNull(groups = OnAdd.class)
    @NullOrNotBlank
    @Size(max = 200)
    String description;

    @NotNull(groups = OnAdd.class)
    Boolean available;

    Long requestId;
}
