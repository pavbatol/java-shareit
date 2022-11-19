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
import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    protected static final String ENTITY_SIMPLE_NAME = "Пользователь";

    private final UserStorage userStorage;
    @Override
    public UserDto add(@NotNull UserDto userDto) {
        // validateEntity(user);
        User added = userStorage.add(toUser(userDto));
        log.debug("Добавлен {}: {}", ENTITY_SIMPLE_NAME, added);
        return toUserDto(added);
    }

    @Override
    public UserDto update(@NotNull UserDto userDto, Long id) {
        // validateEntity(user);
        User updated = userStorage.update(toUser(userDto, getNonNullObject(userStorage, id)));
        log.debug("Обновлен {}: {}", ENTITY_SIMPLE_NAME, updated);
        return toUserDto(updated);
    }

    @Override
    public UserDto remove(Long id) {
        User removed = userStorage.remove(id);
        log.debug("Удален {}: {}", ENTITY_SIMPLE_NAME, removed);
        return toUserDto(removed);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> found = userStorage.findAll();
        log.debug("Текущий размер списка для {}: {}", ENTITY_SIMPLE_NAME, found.size());
        return found.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        User found = getNonNullObject(userStorage, id);
        log.debug("Найден {}: {}", ENTITY_SIMPLE_NAME, found);
        return toUserDto(found);
    }
}
