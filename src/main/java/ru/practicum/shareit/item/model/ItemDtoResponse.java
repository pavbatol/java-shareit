package ru.practicum.shareit.item.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.booking.model.BookingDtoShort;
import ru.practicum.shareit.item.comment.model.CommentDtoShort;

import java.util.Set;

@Value
public class ItemDtoResponse {

    Long id;

    String name;

    String description;

    Boolean available;

    @NonFinal
    @Setter
    BookingDtoShort lastBooking;

    @NonFinal
    @Setter
    BookingDtoShort nextBooking;

    @NonFinal
    @Setter
    Set<CommentDtoShort> comments;
}
