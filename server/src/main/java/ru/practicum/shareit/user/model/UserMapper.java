package ru.practicum.shareit.user.model;

import ru.practicum.shareit.common.Mapper;

@org.mapstruct.Mapper(componentModel = "spring")
public interface UserMapper extends Mapper<User, UserDto> {

}
