package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;

import javax.validation.constraints.NotNull;
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
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto add(BookingAddDto dto, Long userId) {
        dto.setStatus(BookingStatus.WAITING);
        Booking booking = bookingMapper.toEntityFilledRelations(dto, userId);
        checkAvailable(booking);
        checkDates(booking);
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
        orig.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking saved = bookingRepository.save(orig);
        return bookingMapper.toDto(saved);
    }

    @Override
    public BookingDto findById(Long bookingId, Long userId) {
        Booking booking = getNonNullObject(bookingRepository, bookingId);
        if (!Objects.equals(userId, booking.getItem().getOwner().getId())
                && !Objects.equals(userId, booking.getBooker().getId())) {
            throw new ValidationException("Only the item owner or the booker can view");
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> findAllByBookerId(Long bookerId, String byState) {
        return null;
    }

    @Override
    public List<BookingDto> findAllByOwnerId(Long ownerId, String byState) {
        return null;
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

    private static void checkDates(@NotNull Booking booking) {
        LocalDateTime start = Objects.requireNonNull(booking.getStart());
        LocalDateTime end = Objects.requireNonNull(booking.getEnd());
        if (end.isBefore(start) || end.equals(start)
                || start.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Incorrect booking date-time");
        }
    }

}
