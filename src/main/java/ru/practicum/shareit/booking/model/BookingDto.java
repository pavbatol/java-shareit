package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {

    Long id;

    @NotNull
    UserDto booker;

    @NotNull
    ItemDto item;

    @NotNull
    LocalDate start;

    @NotNull
    LocalDate end;

    @NotNull
    BookingStatus status;
}