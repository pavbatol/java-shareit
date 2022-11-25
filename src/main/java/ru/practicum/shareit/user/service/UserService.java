package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

public interface UserService {
    UserDto add(UserDto userDto);

    UserDto update(UserDto userDto, Long userId);

    UserDto remove(Long userId);

    List<UserDto> findAll();

    UserDto findById(Long userId);
}
