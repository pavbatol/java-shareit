package ru.practicum.shareit.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.OnAdd;
import ru.practicum.shareit.validator.annotated.NullOrNotBlank;

import javax.validation.constraints.Email;
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
public class UserDto {

    @EqualsAndHashCode.Include
    Long id;

    @NotNull(groups = OnAdd.class)
    @NullOrNotBlank
    @Size(max = 50)
    String name;

    @NotBlank(groups = OnAdd.class)
    @NullOrNotBlank
    @Email
    String email;
}
