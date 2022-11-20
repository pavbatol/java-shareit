package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import javax.validation.constraints.NotNull;

public final class ItemMapper {

    private ItemMapper() {
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    @NotNull
    public static Item toItem(@NotNull ItemDto itemDto, @NotNull Item targetItem) {
        return Item.builder()
                .id(targetItem.getId())
                .name(itemDto.getName() == null ? targetItem.getName() : itemDto.getName())
                .description(itemDto.getDescription() == null ? targetItem.getDescription() : itemDto.getDescription())
                .available(itemDto.getAvailable() == null ? targetItem.getAvailable() : itemDto.getAvailable())
                .request(targetItem.getRequest())
                .owner(targetItem.getOwner())
                .build();

    }

    @NotNull
    public static Item toItem(@NotNull ItemDto itemDto, long ownerId) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(null)
                .owner(ownerId)
                .build();

    }
}
