package ru.practicum.shareit.validator.impl;

import ru.practicum.shareit.exeption.ValidateException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validator.Validator;

import java.util.Objects;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User user) throws ValidateException {
        if (Objects.isNull(user)) {
            throw new ValidateException("Полученный объект User не инициализирован");
        }
        if (Objects.isNull(user.getEmail())
                || !user.getEmail().matches("\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*\\.\\w{2,4}")) {
            throw new ValidateException("Некорректный email");
        }
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            throw new ValidateException("Name не может быть пустым");
        }
    }
}
