package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.model.enums.BookingStatus;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class BookingDtoAdd {

    Long itemId;

    LocalDateTime start;

    LocalDateTime end;

    BookingStatus status;
}