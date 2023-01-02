package ru.practicum.shareit.validator.annotated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrNotBlank {
    String message() default "The field must contain at least one non-whitespace character or be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
