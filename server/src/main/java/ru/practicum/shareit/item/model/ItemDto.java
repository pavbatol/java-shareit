package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemDto {

    Long id;

    String name;

    String description;

    Boolean available;

    Long requestId;
}
