package ru.practicum.shareit.user.db;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.util.*;

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
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User remove(Long id) {
        return users.remove(id);
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
    public boolean containsEmail(String email, Long exceptUserId) {
        return users.values().stream()
                .filter(us -> !Objects.equals(us.getId(), exceptUserId))
                .map(User::getEmail)
                .filter(Objects::nonNull)
                .anyMatch(str -> str.equals(email));
    }
}
