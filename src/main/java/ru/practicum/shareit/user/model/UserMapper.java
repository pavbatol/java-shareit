package ru.practicum.shareit.user.model;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.user.storage.UserRepository;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class UserMapper implements Mapper<User, UserDto> {

    @Autowired
    protected UserRepository userRepository;

    public User toEntity(Long userId) {
        return getNonNullObject(userRepository, userId);
    }
}
