package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingDtoAdd;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.enums.BookingStatus;
import ru.practicum.shareit.booking.model.enums.BookingsByStateFactory;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    protected static final String ENTITY_SIMPLE_NAME = "Booking";
    public static final String START = "start";
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto add(BookingDtoAdd dto, Long userId) {
        Booking booking = bookingMapper.toEntityFilledRelations(dto, userId, userRepository, itemRepository);
        checkUserNotOwner(userId, booking.getItem().getOwner().getId());
        checkAvailable(booking);
        checkDates(booking);
        booking.setStatus(BookingStatus.WAITING);
        Booking saved = bookingRepository.save(booking);
        log.debug("Added {}: {}", ENTITY_SIMPLE_NAME, saved);
        return bookingMapper.toDto(saved);
    }

    @Override
    public BookingDto approve(Long bookingId, Long userId, Boolean approved) {
        Booking orig = getNonNullObject(bookingRepository, bookingId);
        if (!Objects.equals(userId, orig.getItem().getOwner().getId())) {
            throw new NotFoundException("Only owner of item can approve");
        }
        if (Objects.equals(
                approved ? BookingStatus.APPROVED : BookingStatus.REJECTED,
                orig.getStatus())) {
            throw new ValidationException("The status is already set: " + orig.getStatus());
        }
        orig.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking saved = bookingRepository.save(orig);
        return bookingMapper.toDto(saved);
    }

    @Override
    public BookingDto findById(Long bookingId, Long userId) {
        Booking booking = getNonNullObject(bookingRepository, bookingId);
        if (!Objects.equals(userId, booking.getItem().getOwner().getId())
                && !Objects.equals(userId, booking.getBooker().getId())) {
            throw new NotFoundException("Only the item owner or the booker can view");
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> findAllByBookerId(Long bookerId, String state, int from, int size) {
        Sort sort = Sort.by(START).descending();
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        BookingsByStateFactory factory = BookingsByStateFactory.getFactory(bookingRepository);
        List<Booking> bookings = factory.getForBooker(bookerId, state, pageRequest);
        if (bookings.isEmpty()) {
            throw new NotFoundException("Bookings for bookerId=" + bookerId + " not found");
        }
        return bookingMapper.toDtos(bookings);
    }

    @Override
    public List<BookingDto> findAllByOwnerId(Long ownerId, String state, int from, int size) {
        Sort sort = Sort.by(START).descending();
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        BookingsByStateFactory factory = BookingsByStateFactory.getFactory(bookingRepository);
        List<Booking> bookings = factory.getForOwner(ownerId, state, pageRequest);
        if (bookings.isEmpty()) {
            throw new NotFoundException("Bookings for bookerId=" + ownerId + " not found");
        }
        return bookingMapper.toDtos(bookings);
    }

    private void checkAvailable(Booking booking) {
        Optional.ofNullable(booking.getItem()).ifPresentOrElse(
                item -> {
                    if (Objects.isNull(item.getAvailable()) || !item.getAvailable()) {
                        throw new ValidationException("The item is not available for booking");
                    }
                },
                () -> {
                    throw new RuntimeException("Item is null");
                });
    }

    private void checkDates(Booking booking) {
        LocalDateTime start = Objects.requireNonNull(booking.getStart());
        LocalDateTime end = Objects.requireNonNull(booking.getEnd());
        if (end.isBefore(start) || end.equals(start) || start.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Incorrect booking date-time");
        }
    }

    private void checkUserNotOwner(Long userId, Long ownerId) {
        if (Objects.equals(userId, ownerId)) {
            throw new NotFoundException("The owner of the item does not need to book it");
        }
    }
}
