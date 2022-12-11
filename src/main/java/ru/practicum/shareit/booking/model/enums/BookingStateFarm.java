package ru.practicum.shareit.booking.model.enums;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.IllegalEnumException;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static ru.practicum.shareit.booking.model.enums.BookingState.*;

public final class BookingStateFarm {
    private final BookingRepository repository;
    private final Long userId;
    private final Map<BookingState, Supplier<List<Booking>>> bookerSuppliers;
    private final Map<BookingState, Supplier<List<Booking>>> ownerSuppliers;

    private BookingStateFarm(BookingRepository repository, Long userId) {
        this.repository = Objects.requireNonNull(repository);
        this.userId = Objects.requireNonNull(userId);
        this.bookerSuppliers = setBookingSSuppliers();
        this.ownerSuppliers = setOwnerSSuppliers();
    }

    private Map<BookingState, Supplier<List<Booking>>> setBookingSSuppliers() {
        return Map.of(
                ALL, () -> repository.findAllByBookerId(userId),
                WAITING, () -> repository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING),
                REJECTED, () -> repository.findAllByBookerIdAndStatus(userId, BookingStatus.REJECTED),
                PAST, () -> repository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now()),
                CURRENT, () -> repository.findAllByBookerIdAndStartBeforeAndEndAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now()),
                FUTURE, () -> repository.findAllByBookerIdAndStartAfter(userId, LocalDateTime.now())
        );
    }

    private Map<BookingState, Supplier<List<Booking>>> setOwnerSSuppliers() {
        return Map.of(
                ALL, () -> repository.findAllByItemOwnerId(userId),
                WAITING, () -> repository.findAllByItemOwnerIdAndStatus(userId, BookingStatus.WAITING),
                REJECTED, () -> repository.findAllByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED),
                PAST, () -> repository.findAllByItemOwnerIdAndEndBefore(userId, LocalDateTime.now()),
                CURRENT, () -> repository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now()),
                FUTURE, () -> repository.findAllByItemOwnerIdAndStartAfter(userId, LocalDateTime.now())
        );
    }

    @NotNull
    public List<Booking> getBookingsByState(BookingState state, boolean isBooker) {
        List<Booking> bookings = isBooker
                ? bookerSuppliers.get(state).get()
                : ownerSuppliers.get(state).get();
        if (Objects.isNull(bookings)) {
            throw new IllegalEnumException("No mapping for the state: " + state);
        }
        return bookings;
    }

    @NotNull
    public List<Booking> getBookingsByState(String stateName, boolean isBooker) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(stateName);
        } catch (IllegalArgumentException e) {
            throw new IllegalEnumException("Unknown state: " + stateName);
        }
        return getBookingsByState(bookingState, isBooker);
    }

    public static BookingStateFarm getFarm(BookingRepository repository, Long userId) {
        return new BookingStateFarm(repository, userId);
    }
}

