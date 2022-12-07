package ru.practicum.shareit.item.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.booking.model.BookingShortDto;

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
}
