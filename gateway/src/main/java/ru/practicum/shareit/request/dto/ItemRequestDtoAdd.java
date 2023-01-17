package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemRequestDtoAdd {

    Long id;

    @NotBlank
    @Size(max = 512)
    String description;
}
