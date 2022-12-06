package ru.practicum.shareit.booking.model;

import org.mapstruct.Mapping;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.UserMapper;

@org.mapstruct.Mapper(componentModel = "spring",
        uses = {UserMapper.class, ItemMapper.class})
public interface BookingMapper extends Mapper<Booking, BookingDto> {

    @Mapping(target = "item.id", source = "dto.itemId")
    @Mapping(target = "booker.id", source = "bookerId")
    Booking toEntity(BookingAddDto dto, Long bookerId);

    @Mapping(target = "item", source = "dto.itemId")
    @Mapping(target = "booker", source = "bookerId")
    Booking toEntityFilledRelations(BookingAddDto dto, Long bookerId);

    @Mapping(target = "item", source = "dto.itemId")
    @Mapping(target = "booker.id", source = "bookerId")
    Booking toEntityFilledItem(BookingAddDto dto, Long bookerId);

}
