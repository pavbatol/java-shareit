package ru.practicum.shareit.item.model;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.booking.model.BookingDtoShort;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Objects;

@org.mapstruct.Mapper(componentModel = "spring")
public interface ItemMapper extends Mapper<Item, ItemDto> {

    @Override
    @Mapping(target = "requestId", source = "entity.request.id")
    ItemDto toDto(Item entity);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "request", source = "dto.requestId", qualifiedByName = "setRequest")
    Item toEntity(ItemDto dto, User owner);

    @Named("setRequest")
    default ItemRequest getItemRequest(Long id) {
        return !Objects.isNull(id) ? ItemRequest.builder().id(id).build() : null;
    }

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "lastBooking", source = "last")
    @Mapping(target = "nextBooking", source = "next")
    ItemDtoResponse toResponseDto(Item entity, BookingDtoShort last, BookingDtoShort next);
}
