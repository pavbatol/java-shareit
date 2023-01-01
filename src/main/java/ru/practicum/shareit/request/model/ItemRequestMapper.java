package ru.practicum.shareit.request.model;

import org.mapstruct.Mapping;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@org.mapstruct.Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface ItemRequestMapper extends Mapper<ItemRequest, ItemRequestDto> {

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "requester", source = "user")
    @Mapping(target = "created", source = "created")
    ItemRequest toEntity(ItemRequestDto dto, User user, LocalDateTime created);
}
