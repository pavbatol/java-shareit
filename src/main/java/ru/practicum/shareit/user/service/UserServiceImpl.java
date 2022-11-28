package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.practicum.shareit.validator.ValidatorManager.checkDuplicatedEmail;
import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    protected static final String ENTITY_SIMPLE_NAME = "User";
    private final UserStorage userStorage;
    private final UserMapper mapper;

    @Override
    public UserDto add(@NotNull UserDto userDto) {
        checkDuplicatedEmail(userStorage, userDto.getEmail(), null);
        User added = userStorage.add(mapper.toEntity(userDto));
        log.debug("Added {}: {}", ENTITY_SIMPLE_NAME, added);
        return mapper.toDto(added);
    }

    @Override
    public UserDto update(@NotNull UserDto userDto, Long userId) {
        checkDuplicatedEmail(userStorage, userDto.getEmail(), userDto.getId());
        User entity = mapper.toEntity(userDto, getNonNullObject(userStorage, userId));
        User updated = userStorage.update(entity);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);
        return mapper.toDto(updated);
    }

    @Override
    public UserDto remove(Long userId) {
        User removed = userStorage.remove(userId);
        log.debug("Removed {}: {}", ENTITY_SIMPLE_NAME, removed);
        return mapper.toDto(removed);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> found = userStorage.findAll();
        log.debug("Тhe current size of the list for {}: {}", ENTITY_SIMPLE_NAME, found.size());
        return mapper.toDtos(found);
    }

    @Override
    public UserDto findById(Long userId) {
        User found = getNonNullObject(userStorage, userId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);
        return mapper.toDto(found);
    }
}
