package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.common.Entity;
import ru.practicum.shareit.common.OnAdd;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto implements Entity {

    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(groups = OnAdd.class)
    private String name;

    @NotBlank(groups = OnAdd.class)
    @Size(max = 200)
    private String description;

    @NotNull(groups = OnAdd.class)
    private Boolean available;
}
