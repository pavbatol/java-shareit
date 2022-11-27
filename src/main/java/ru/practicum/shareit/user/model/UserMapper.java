package ru.practicum.shareit.user.model;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public final class UserMapper {

    @NotNull
    public static UserDto toUserDto(@NotNull User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @NotNull
    public static User toUser(@NotNull UserDto userDto, @NotNull User targetUser) {
        return targetUser.toBuilder()
                .name(userDto.getName() == null ? targetUser.getName() : userDto.getName())
                .email(userDto.getEmail() == null ? targetUser.getEmail() : userDto.getEmail())
                .build();
    }

    @NotNull
    public static User toUser(@NotNull UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
