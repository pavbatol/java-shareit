package ru.practicum.shareit.item.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.booking.model.BookingShortDto;
import ru.practicum.shareit.item.comment.model.CommentShortDto;

import java.util.Set;

@Value
public class ItemBookingDto {

    Long id;

    String name;

    String description;

    Boolean available;

    @NonFinal
    @Setter
    BookingShortDto lastBooking;

    @NonFinal
    @Setter
    BookingShortDto nextBooking;

    @NonFinal
    @Setter
    Set<CommentShortDto> comments;
}
