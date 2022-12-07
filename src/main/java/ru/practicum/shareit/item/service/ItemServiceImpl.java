package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingShortDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemBookingDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    protected static final String ENTITY_SIMPLE_NAME = "Thing";
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;

    @Override
    public ItemDto add(ItemDto itemDto, Long userId) {
        Item enrichedItemDto = itemMapper.toEntity(itemDto, getNonNullObject(userRepository, userId));
        Item added = itemRepository.save(enrichedItemDto);
        log.debug("Added {}: {}", ENTITY_SIMPLE_NAME, added);
        return itemMapper.toDto(added);
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        Item orig = getNonNullObject(itemRepository, itemId);
        if (!Objects.equals(orig.getOwner().getId(), userId)) {
            throw new NotFoundException("Only owner can edit");
        }
        Item updated = itemRepository.save(itemMapper.updateEntity(itemDto, orig));
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);
        return itemMapper.toDto(updated);
    }

    @Override
    public List<ItemBookingDto> findAllByUserId(Long userId) {
        List<Item> found = itemRepository.findAllByOwner_IdOrderByIdAsc(userId);
        log.debug("The current size of the list for {}: {}", ENTITY_SIMPLE_NAME, found.size());
        return found.stream()
                .map(this::getWithBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemBookingDto findById(Long itemId, Long userId) {
        Item found = getNonNullObject(itemRepository, itemId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);
        if (Objects.equals(userId, found.getOwner().getId())) {
            return getWithBookingDto(found);
        }
        return itemMapper.toWithBookingDto(found, null, null);
    }

    @Override
    public List<ItemDto> searchByNameOrDescription(String text) {
        List<Item> searched = StringUtils.isBlank(text)
                ? Collections.emptyList()
                : itemRepository.searchByNameOrDescription(text);
        return itemMapper.toDtos(searched);
    }

    private ItemBookingDto getWithBookingDto(@NotNull Item item) {
        LocalDateTime current = LocalDateTime.now();
        List<Booking> itemBookings = bookingRepository.findByItem_Id(item.getId());

        BookingShortDto last = bookingMapper.toShortDto(itemBookings.stream()
                .filter(booking -> booking.getEnd().isBefore(current))
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(null));

        BookingShortDto next = bookingMapper.toShortDto(itemBookings.stream()
                .filter(booking -> booking.getStart().isAfter(current))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null));

        return itemMapper.toWithBookingDto(item, last, next);
    }
}
