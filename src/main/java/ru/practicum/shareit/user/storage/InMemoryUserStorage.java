package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.AbstractInMemoryStorage;
import ru.practicum.shareit.user.model.User;

import java.util.Objects;

@Repository("inMemoryUserStorage")
public class InMemoryUserStorage extends AbstractInMemoryStorage<User> implements UserStorage {

    @Override
    public boolean containsEmail(String email, Long exceptUserId) {
        return container.values().stream()
                .filter(us -> !Objects.equals(us.getId(), exceptUserId))
                .map(User::getEmail)
                .filter(Objects::nonNull)
                .anyMatch(str -> str.equals(email));
    }
}
