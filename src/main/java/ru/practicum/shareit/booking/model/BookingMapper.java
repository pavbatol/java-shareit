package ru.practicum.shareit.booking.model;

import org.mapstruct.Context;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.common.GroupService;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

@org.mapstruct.Mapper(componentModel = "spring",
        uses = {UserMapper.class, ItemMapper.class})
public interface BookingMapper extends Mapper<Booking, BookingDto> {

    @Mapping(target = "item.id", source = "dto.itemId")
    @Mapping(target = "booker.id", source = "bookerId")
    Booking toEntity(BookingAddDto dto, Long bookerId);

    @Mapping(target = "item", source = "dto.itemId", qualifiedByName = "itemById")
    @Mapping(target = "booker", source = "bookerId", qualifiedByName = "userById")
    Booking toEntityFilledRelations(BookingAddDto dto, Long bookerId, @Context GroupService groupService);

    @Named("userById")
    default User setUser(Long id, @Context GroupService groupService) {
        return groupService.getUser(id);
    }

    @Named("itemById")
    default Item setItem(Long id, @Context GroupService groupService) {
        return groupService.getItem(id);
    }

    @Mapping(target = "bookerId", source = "booker.id")
    BookingShortDto toShortDto(Booking entity);
}
