package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.OnAdd;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {

    @EqualsAndHashCode.Include
    Long id;

    @NotBlank(groups = OnAdd.class)
    String name;

    @NotBlank(groups = OnAdd.class)
    @Size(max = 200)
    String description;

    @NotNull(groups = OnAdd.class)
    Boolean available;
}
