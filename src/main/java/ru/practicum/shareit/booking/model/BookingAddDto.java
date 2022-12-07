package ru.practicum.shareit.booking.model;

import lombok.Value;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class BookingAddDto {

    @NotNull
    Long itemId;

    @NotNull
    @Future
    LocalDateTime start;

    @NotNull
    @Future
    LocalDateTime end;

    BookingStatus status;
}