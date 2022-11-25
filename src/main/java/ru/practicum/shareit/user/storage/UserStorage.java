package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.common.CrudStorage;
import ru.practicum.shareit.user.model.User;

public interface UserStorage extends CrudStorage<User> {

    boolean containsEmail(String email, Long exceptUserId);
}
