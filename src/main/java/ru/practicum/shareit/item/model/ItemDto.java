package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {

    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;

    @NotNull
    private Boolean available;
}
