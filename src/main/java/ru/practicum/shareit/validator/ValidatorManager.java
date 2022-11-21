package ru.practicum.shareit.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.common.CrudStorage;
import ru.practicum.shareit.common.Entity;
import ru.practicum.shareit.exeption.AlreadyExistsException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.db.UserStorage;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Slf4j
public final class ValidatorManager {
    private ValidatorManager() {
    }

    public static void validateId(@NotNull CrudStorage<?> storage, Long id) {
        if (!storage.contains(id)) {
            throw new NotFoundException(String.format("id %s не найден", id));
        }
    }

    public static void validateId(@NotNull CrudStorage<?> storage, Long id, boolean checkedForNull) {
        if (checkedForNull && Objects.isNull(id)) {
            throw new RuntimeException(String.format("id не должен быть %s", id));
        }
        validateId(storage, id);
    }

    public static void validateId(@NotNull CrudStorage<?> storage, @NotNull Entity entity, @Nullable String message) {
        if (!storage.contains(entity.getId())) {
            if (Objects.isNull(message) || message.isBlank()) {
                message = String.format("Такого id для %s нет: %s", entity.getClass().getSimpleName(), entity.getId());
            }
            log.error(message);
            throw new NotFoundException(message);
        }
    }

    @NotNull
    public static <T> T getNonNullObject(@NotNull CrudStorage<T> storage, Long id) throws NotFoundException {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Объект по id %s не найден", id)));
    }

    public static void checkDuplicatedEmail(@NotNull UserStorage userStorage, String email, Long exceptUserId) {
        if (Objects.nonNull(email) && userStorage.containsEmail(email, exceptUserId)) {
            throw new AlreadyExistsException("Такой email уже есть: " + email);
        }
    }
}
