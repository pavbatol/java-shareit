package ru.practicum.shareit.user.db;

import ru.practicum.shareit.common.Storage;
import ru.practicum.shareit.user.model.User;

public interface UserStorage extends Storage<User> {

    boolean containsEmail(String email);
}
