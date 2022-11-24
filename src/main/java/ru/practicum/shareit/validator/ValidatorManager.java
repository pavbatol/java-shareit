package ru.practicum.shareit.validator;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.common.CrudStorage;
import ru.practicum.shareit.exeption.AlreadyExistsException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.db.UserStorage;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Slf4j
public final class ValidatorManager {

    @NotNull
    public static <T> T getNonNullObject(@NotNull CrudStorage<T> storage, Long id) throws NotFoundException {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Object by id %s not found", id)));
    }

    public static void checkDuplicatedEmail(@NotNull UserStorage userStorage, String email, Long exceptUserId) {
        if (Objects.nonNull(email) && userStorage.containsEmail(email, exceptUserId)) {
            throw new AlreadyExistsException("There already exists this email: " + email);
        }
    }
}
