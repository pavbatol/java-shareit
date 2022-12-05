package ru.practicum.shareit.booking.model;

import org.mapstruct.Mapping;
import ru.practicum.shareit.common.Mapper;

@org.mapstruct.Mapper(componentModel = "spring")
public interface BookingMapper extends Mapper<Booking, BookingDto> {

    @Mapping(target = "item.id", source = "dto.itemId")
    @Mapping(target = "booker.id", source = "bookerId")
    Booking toEntity(BookingAddDto dto, Long bookerId);

//    @Mapping(target = "booker.id", source = "bookerId")
//    Booking toEntity(BookingDto dto, Long bookerId);
}
