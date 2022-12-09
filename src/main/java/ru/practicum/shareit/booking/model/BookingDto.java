package ru.practicum.shareit.booking.model;

import lombok.Value;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Value
public class BookingDto {

    Long id;

    UserDto booker;

    ItemDto item;

    LocalDateTime start;

    LocalDateTime end;

    BookingStatus status;
}