package ru.practicum.shareit.validator.impl;

import ru.practicum.shareit.exeption.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.validator.Validator;

import java.util.Objects;

public class ItemValidator implements Validator<Item> {
    @Override
    public void validate(Item item) throws ValidateException {
        if (Objects.isNull(item)) {
            throw new ValidateException("Полученный объект Item не инициализирован");
        }
        if (item.getName().isBlank()) {
            throw new ValidateException("Имя не должно быть пустым");
        }
        if (Objects.nonNull(item.getDescription()) && item.getDescription().length() > 200) {
            throw new ValidateException("Максимальная длина описания 200 символов");
        }
    }
}
