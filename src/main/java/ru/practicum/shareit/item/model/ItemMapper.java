package ru.practicum.shareit.item.model;

import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.user.model.User;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class ItemMapper implements Mapper<Item, ItemDto> {

    public Item toEntity(ItemDto itemDto, User owner) {
        Item item = toEntity(itemDto);
        item.setOwner(owner);
        return item;
    }
}
