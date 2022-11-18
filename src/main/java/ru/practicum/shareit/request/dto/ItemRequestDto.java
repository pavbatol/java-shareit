package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequestDto {

    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;
}
