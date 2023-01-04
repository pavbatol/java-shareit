package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class UserDtoUpdate {

    Long id;

    @Pattern(regexp = ".*\\S.*")
    @Size(max = 50)
    String name;

    @Pattern(regexp = ".*\\S.*")
    @Email
    String email;
}
