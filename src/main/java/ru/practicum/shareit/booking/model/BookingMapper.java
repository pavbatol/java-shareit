package ru.practicum.shareit.booking.model;

import org.mapstruct.Context;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@org.mapstruct.Mapper(componentModel = "spring",
        uses = {UserMapper.class, ItemMapper.class})
public interface BookingMapper extends Mapper<Booking, BookingDto> {

    @Mapping(target = "item", source = "dto.itemId", qualifiedByName = "itemById")
    @Mapping(target = "booker", source = "bookerId", qualifiedByName = "userById")
    Booking toEntityFilledRelations(BookingDtoAdd dto, Long bookerId,
                                    @Context UserRepository userRepository,
                                    @Context ItemRepository itemRepository);

    @Named("userById")
    default User idToUser(Long id, @Context UserRepository userRepository) {
        return getNonNullObject(userRepository, id);
    }

    @Named("itemById")
    default Item idToItem(Long id, @Context ItemRepository itemRepository) {
        return getNonNullObject(itemRepository, id);
    }

    @Mapping(target = "bookerId", source = "booker.id")
    BookingDtoShort toShortDto(Booking entity);
}
