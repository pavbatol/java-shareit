package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemDtoUpdate {

    Long id;

    @Pattern(regexp = ".*\\S.*")
    @Size(max = 50)
    String name;

    @Pattern(regexp = ".*\\S.*")
    @Size(max = 200)
    String description;

    Boolean available;

    Long requestId;
}
