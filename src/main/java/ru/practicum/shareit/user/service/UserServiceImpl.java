package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    protected static final String ENTITY_SIMPLE_NAME = "User";
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserDto add(@NotNull UserDto userDto) {
        User added = userRepository.save(mapper.toEntity(userDto));
        log.debug("Added {}: {}", ENTITY_SIMPLE_NAME, added);
        return mapper.toDto(added);
    }

    @Override
    public UserDto update(@NotNull UserDto userDto, Long userId) {
        User entity = mapper.updateEntity(userDto, getNonNullObject(userRepository, userId));
        User updated = userRepository.save(entity);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);
        return mapper.toDto(updated);
    }

    @Override
    public UserDto remove(Long userId) {
        User removed = getNonNullObject(userRepository, userId);
        userRepository.delete(removed);
        log.debug("Removed {}: {}", ENTITY_SIMPLE_NAME, removed);
        return mapper.toDto(removed);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> found = userRepository.findAll();
        log.debug("Тhe current size of the list for {}: {}", ENTITY_SIMPLE_NAME, found.size());
        return mapper.toDtos(found);
    }

    @Override
    public UserDto findById(Long userId) {
        User found = getNonNullObject(userRepository, userId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);
        return mapper.toDto(found);
    }
}
