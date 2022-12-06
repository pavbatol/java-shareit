package ru.practicum.shareit.item.model;

import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class ItemMapper implements Mapper<Item, ItemDto> {

    @Autowired
    protected ItemRepository itemRepository;

    @Mapping(target = "owner.id", source = "userId")
    public abstract Item toEntity(ItemDto itemDto, Long userId);

    public Item toEntity(ItemDto itemDto, User owner) {
        Item item = toEntity(itemDto);
        item.setOwner(owner);
        return item;
    }

    public Item toEntity(Long itemId) {
        return getNonNullObject(itemRepository, itemId);
    }
}
