package ru.practicum.shareit.item.model;

import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;

public final class ItemMapper {

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
        return targetItem.toBuilder()
                .name(itemDto.getName() == null ? targetItem.getName() : itemDto.getName())
                .description(itemDto.getDescription() == null ? targetItem.getDescription() : itemDto.getDescription())
                .available(itemDto.getAvailable() == null ? targetItem.getAvailable() : itemDto.getAvailable())
                .build();
    }

    @NotNull
    public static Item toItem(@NotNull ItemDto itemDto, User owner) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .requestId(null)
                .owner(owner)
                .build();
    }
}
