package ru.practicum.shareit.validator;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.Entity;
import ru.practicum.shareit.Storage;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Slf4j
public final class ValidatorManager {
    private ValidatorManager() {
    }

    public static <T extends Entity> void validateEntity(T t) {
        Class<? extends Entity> clazz = t.getClass();
        if (clazz == Item.class) {
            // new ItemValidator().runValidation((Item) t);
        } else if (clazz == User.class) {
            // new UserValidator().runValidation((User) t);
        }
    }

    public static void validateId(@NotNull Storage<?> storage, Long id) {
        if (!storage.contains(id)) {
            throw new NotFoundException(String.format("id %s не найден", id));
        }
    }

    public static void validateId(@NotNull Storage<?> storage, Long id, boolean checkedForNull) {
        if (checkedForNull && Objects.isNull(id)) {
            throw new RuntimeException(String.format("id не должен быть %s", id));
        }
        validateId(storage, id);
    }

    public static void validateId(@NotNull Storage<?> storage, @NotNull Entity entity, @Nullable String message) {
        if (!storage.contains(entity.getId())) {
            if (Objects.isNull(message) || message.isBlank()) {
                message = String.format("Такого id для %s нет: %s", entity.getClass().getSimpleName(), entity.getId());
            }
            log.error(message);
            throw new NotFoundException(message);
        }
    }

    @NotNull
    public static <T> T getNonNullObject(@NotNull Storage<T> storage, Long id) throws NotFoundException {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Объект по id %s не найден", id)));
    }
}
