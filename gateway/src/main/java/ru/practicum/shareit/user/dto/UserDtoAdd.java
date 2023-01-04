package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class UserDtoAdd {

    Long id;

    @NotBlank
    @Size(max = 50)
    String name;

    @NotBlank
    @Email
    String email;
}
