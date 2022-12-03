package ru.practicum.shareit.validator.annotated;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.isNull(str) || !str.isBlank();
    }
}
