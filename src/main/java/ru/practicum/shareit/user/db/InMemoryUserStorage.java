package ru.practicum.shareit.user.db;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.util.*;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;
import static ru.practicum.shareit.validator.ValidatorManager.validateId;

@Repository("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(@NotNull User user) {
        long id = users.keySet().stream()
                        .max(Long::compare)
                        .orElse(0L) + 1;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(@NotNull User user) {
        validateId(this, user, null);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User remove(Long id) {
        User user = getNonNullObject(this, id);
        users.remove(id);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean contains(Long id) {
        return users.containsKey(id);
    }
}
