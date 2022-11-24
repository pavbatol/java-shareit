package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.db.UserStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserMapper.toUser;
import static ru.practicum.shareit.user.UserMapper.toUserDto;
import static ru.practicum.shareit.validator.ValidatorManager.checkDuplicatedEmail;
import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    protected static final String ENTITY_SIMPLE_NAME = "User";

    private final UserStorage userStorage;

    @Override
    public UserDto add(@NotNull UserDto userDto) {
        checkDuplicatedEmail(userStorage, userDto.getEmail(), null);
        User added = userStorage.add(toUser(userDto));
        log.debug("Added {}: {}", ENTITY_SIMPLE_NAME, added);
        return toUserDto(added);
    }

    @Override
    public UserDto update(@NotNull UserDto userDto, Long userId) {
        checkDuplicatedEmail(userStorage, userDto.getEmail(), userDto.getId());
        User updated = userStorage.update(toUser(userDto, getNonNullObject(userStorage, userId)));
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);
        return toUserDto(updated);
    }

    @Override
    public UserDto remove(Long userId) {
        User removed = userStorage.remove(userId);
        log.debug("Removed {}: {}", ENTITY_SIMPLE_NAME, removed);
        return toUserDto(removed);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> found = userStorage.findAll();
        log.debug("ТThe current size of the list for {}: {}", ENTITY_SIMPLE_NAME, found.size());
        return found.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long userId) {
        User found = getNonNullObject(userStorage, userId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);
        return toUserDto(found);
    }
}
