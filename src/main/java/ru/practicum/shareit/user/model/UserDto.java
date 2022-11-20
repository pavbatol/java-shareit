package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.common.Entity;
import ru.practicum.shareit.common.OnAdd;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDto implements Entity {

    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(groups = OnAdd.class)
    @Size(max = 50)
    private String name;

    @NotBlank(groups = OnAdd.class)
    @Email
    private String email;
}
