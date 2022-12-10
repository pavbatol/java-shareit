package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.common.GroupService;
import ru.practicum.shareit.exeption.IllegalEnumException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    protected static final String ENTITY_SIMPLE_NAME = "Booking";
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final GroupService groupService;

    @Override
    public BookingDto add(BookingAddDto dto, Long userId) {
        Booking booking = bookingMapper.toEntityFilledRelations(dto, userId, groupService);
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
    public List<BookingDto> findAllByBookerId(Long bookerId, String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalEnumException("Unknown state: " + state);
        }
        List<Booking> bookings = new ArrayList<>();
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(bookerId);
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId,
                        BookingStatus.valueOf(bookingState.name()));
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndIsBefore(bookerId, LocalDateTime.now());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(bookerId,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartIsAfter(bookerId, LocalDateTime.now());
                break;
        }
        if (bookings.isEmpty()) {
            throw new NotFoundException("Bookings for bookerId=" + bookerId + " not found");
        }
        bookings.sort(Comparator.comparing(Booking::getStart).reversed());
        return bookingMapper.toDtos(bookings);
    }

    @Override
    public List<BookingDto> findAllByOwnerId(Long ownerId, String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalEnumException("Unknown state: " + state);
        }
        List<Booking> bookings = new ArrayList<>();
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByItemOwnerId(ownerId);
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(ownerId,
                        BookingStatus.valueOf(bookingState.name()));
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemOwnerIdAndEndIsBefore(ownerId, LocalDateTime.now());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(ownerId,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartIsAfter(ownerId, LocalDateTime.now());
                break;
        }
        if (bookings.isEmpty()) {
            throw new NotFoundException("Bookings for bookerId=" + ownerId + " not found");
        }
        bookings.sort(Comparator.comparing(Booking::getStart).reversed());
        return bookingMapper.toDtos(bookings);
    }

    private void checkAvailable(@NotNull Booking booking) {
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

    private void checkDates(@NotNull Booking booking) {
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
