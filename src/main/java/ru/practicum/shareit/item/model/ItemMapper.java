package ru.practicum.shareit.item.model;

import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.BookingShortDto;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.user.model.User;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class ItemMapper implements Mapper<Item, ItemDto> {

    public Item toEntity(ItemDto itemDto, User owner) {
        Item item = toEntity(itemDto);
        item.setOwner(owner);
        return item;
    }

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "lastBooking", source = "last")
    @Mapping(target = "nextBooking", source = "next")
    public abstract ItemResponseDto toResponseDto(Item entity, BookingShortDto last, BookingShortDto next);
}
