package ru.practicum.shareit.booking.model.enums;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.IllegalEnumException;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@RequiredArgsConstructor
public final class BookingStateFarm {
    private final BookingRepository repository;
    private final Map<State, Function<Long, List<Booking>>> bookerFunctions = fillBookerFunctions();
    private final Map<State, Function<Long, List<Booking>>> ownerFunctions = fillOwnerFunctions();

    private Map<State, Function<Long, List<Booking>>> fillBookerFunctions() {
        return Map.of(
                State.ALL, (userId) -> repository.findAllByBookerId(userId),
                State.WAITING, (userId) -> repository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING),
                State.REJECTED, (userId) -> repository.findAllByBookerIdAndStatus(userId, BookingStatus.REJECTED),
                State.PAST, (userId) -> repository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now()),
                State.CURRENT, (userId) -> repository.findAllByBookerIdAndStartBeforeAndEndAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now()),
                State.FUTURE, (userId) -> repository.findAllByBookerIdAndStartAfter(userId, LocalDateTime.now())
        );
    }

    private Map<State, Function<Long, List<Booking>>> fillOwnerFunctions() {
        return Map.of(
                State.ALL, (userId) -> repository.findAllByItemOwnerId(userId),
                State.WAITING, (userId) -> repository.findAllByItemOwnerIdAndStatus(userId, BookingStatus.WAITING),
                State.REJECTED, (userId) -> repository.findAllByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED),
                State.PAST, (userId) -> repository.findAllByItemOwnerIdAndEndBefore(userId, LocalDateTime.now()),
                State.CURRENT, (userId) -> repository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now()),
                State.FUTURE, (userId) -> repository.findAllByItemOwnerIdAndStartAfter(userId, LocalDateTime.now())
        );
    }

    @NotNull
    public List<Booking> getBookingsByState(Long userId, State state, boolean isBooker) {
        List<Booking> bookings = isBooker
                ? bookerFunctions.get(state).apply(userId)
                : ownerFunctions.get(state).apply(userId);
        if (Objects.isNull(bookings)) {
            throw new IllegalEnumException("No mapping for the state: " + state);
        }
        return bookings;
    }

    @NotNull
    public List<Booking> getBookingsByState(Long userId, String stateName, boolean isBooker) {
        State state;
        try {
            state = State.valueOf(stateName);
        } catch (IllegalArgumentException e) {
            throw new IllegalEnumException("Unknown state: " + stateName);
        }
        return getBookingsByState(userId, state, isBooker);
    }

    public static BookingStateFarm of(BookingRepository repository) {
        return new BookingStateFarm(repository);
    }

    public enum State {
        ALL,
        CURRENT,
        PAST,
        FUTURE,
        WAITING,
        REJECTED
    }
}