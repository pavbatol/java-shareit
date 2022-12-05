package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.common.Mapper;

@org.mapstruct.Mapper(componentModel = "spring")
public interface BookingMapper extends Mapper<Booking, BookingDto> {
}
