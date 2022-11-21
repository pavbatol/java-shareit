package ru.practicum.shareit.user.db;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.util.*;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;
import static ru.practicum.shareit.validator.ValidatorManager.validateId;

@Repository("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private long lastId = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(@NotNull User user) {
        user.setId(++lastId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(@NotNull User user) {
        validateId(this, user, null);
        String email = getNonNullObject(this, user.getId()).getEmail();
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
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean contains(Long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean containsEmail(String email, Long exceptUserId) {
        return users.values().stream()
                .filter(us -> !Objects.equals(us.getId(), exceptUserId))
                .map(User::getEmail)
                .filter(Objects::nonNull)
                .anyMatch(str -> str.equals(email));
    }
}
