package ru.practicum.shareit.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exeption.NotFoundException;

import javax.validation.constraints.NotNull;

@Slf4j
public final class ValidatorManager {

    @NotNull
    public static <T, I> T getNonNullObject(@NotNull JpaRepository<T, I> storage, I id) throws NotFoundException {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Object by id %s not found", id)));
    }

    public static <T, I> void checkId(@NotNull JpaRepository<T, I> storage, I id, String entitySimpleName) {
        if (!storage.existsById(id)) {
            throw new NotFoundException(String.format("Id #%s not found for %s", id, entitySimpleName));
        }
    }
}
