package ru.practicum.shareit.booking.model;

import lombok.Value;
import ru.practicum.shareit.common.OnAdd;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Value
public class BookingDto {

    Long id;

    UserDto booker;

    @NotNull(groups = OnAdd.class)
    ItemDto item;

    @NotNull(groups = OnAdd.class)
    LocalDateTime start;

    @NotNull(groups = OnAdd.class)
    LocalDateTime end;

    BookingStatus status;
}