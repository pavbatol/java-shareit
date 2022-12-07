package ru.practicum.shareit.item.model;

import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.model.BookingShortDto;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class ItemMapper implements Mapper<Item, ItemDto> {

    @Autowired
    protected ItemRepository itemRepository;

    public Item toEntity(ItemDto itemDto, User owner) {
        Item item = toEntity(itemDto);
        item.setOwner(owner);
        return item;
    }

    public Item toEntity(Long itemId) {
        return getNonNullObject(itemRepository, itemId);
    }

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "lastBooking", source = "last")
    @Mapping(target = "nextBooking", source = "next")
    public abstract ItemBookingDto toWithBookingDto(Item entity, BookingShortDto last, BookingShortDto next);
}
