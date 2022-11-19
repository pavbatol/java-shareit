package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.db.UserStorage;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    protected static final String ENTITY_SIMPLE_NAME = "Пользователь";

    private final UserStorage userStorage;
    @Override
    public User add(@NotNull User user) {
        User added = userStorage.add(user);
        log.debug("Добавлен {}: {}", ENTITY_SIMPLE_NAME, added);
        return added;
    }

    @Override
    public User update(@NotNull User user) {
        User updated = userStorage.update(user);
        log.debug("Обновлен {}: {}", ENTITY_SIMPLE_NAME, updated);
        return updated;
    }

    @Override
    public User remove(Long id) {
        User removed = userStorage.remove(id);
        log.debug("Удален {}: {}", ENTITY_SIMPLE_NAME, removed);
        return removed;
    }

    @Override
    public List<User> findAll() {
        List<User> found = userStorage.findAll();
        log.debug("Текущий размер списка для {}: {}", ENTITY_SIMPLE_NAME, found.size());
        return found;
    }

    @Override
    public User findById(Long id) {
        User found = getNonNullObject(userStorage, id);
        log.debug("Найден {}: {}", ENTITY_SIMPLE_NAME, found);
        return found;
    }
}
