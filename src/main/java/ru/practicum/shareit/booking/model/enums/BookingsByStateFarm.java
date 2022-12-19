package ru.practicum.shareit.booking.model.enums;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.IllegalEnumException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingsByStateFarm {

    private final BookingRepository repository;
    private final Map<State, BiFunction<Long, Pageable, Page<Booking>>> bookerFunctions = fillBookerFunctions();
    private final Map<State, BiFunction<Long, Pageable, Page<Booking>>> ownerFunctions = fillOwnerFunctions();

    private Map<State, BiFunction<Long, Pageable, Page<Booking>>> fillBookerFunctions() {
        return Map.of(
                State.ALL, (userId, pageRequest)
                        -> repository.findAllByBookerId(userId, pageRequest),
                State.WAITING, (userId, pageRequest)
                        -> repository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING, pageRequest),
                State.REJECTED, (userId, pageRequest)
                        -> repository.findAllByBookerIdAndStatus(userId, BookingStatus.REJECTED, pageRequest),
                State.PAST, (userId, pageRequest)
                        -> repository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now(), pageRequest),
                State.CURRENT, (userId, pageRequest)
                        -> repository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), pageRequest),
                State.FUTURE, (userId, pageRequest)
                        -> repository.findAllByBookerIdAndStartAfter(userId, LocalDateTime.now(), pageRequest)
        );
    }

    private Map<State, BiFunction<Long, Pageable, Page<Booking>>> fillOwnerFunctions() {
        return Map.of(
                State.ALL, (userId, pageRequest)
                        -> repository.findAllByItemOwnerId(userId, pageRequest),
                State.WAITING, (userId, pageRequest)
                        -> repository.findAllByItemOwnerIdAndStatus(userId, BookingStatus.WAITING, pageRequest),
                State.REJECTED, (userId, pageRequest)
                        -> repository.findAllByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED, pageRequest),
                State.PAST, (userId, pageRequest)
                        -> repository.findAllByItemOwnerIdAndEndBefore(userId, LocalDateTime.now(), pageRequest),
                State.CURRENT, (userId, pageRequest)
                        -> repository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), pageRequest),
                State.FUTURE, (userId, pageRequest)
                        -> repository.findAllByItemOwnerIdAndStartAfter(userId, LocalDateTime.now(), pageRequest)
        );
    }

    private List<Booking> get(Long userId, State state, Pageable pageable, boolean isBooker) {
        BiFunction<Long, Pageable, Page<Booking>> function = isBooker
                ? bookerFunctions.get(state)
                : ownerFunctions.get(state);
        if (Objects.isNull(function)) {
            throw new IllegalEnumException("No mapping for the state: " + state);
        }
        return function.apply(userId, pageable).getContent();
    }

    private List<Booking> get(Long userId, @NonNull String stateName, Pageable pageable, boolean isBooker) {
        State state;
        try {
            state = State.valueOf(stateName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalEnumException("Unknown state: " + stateName);
        }
        return get(userId, state, pageable, isBooker);
    }

    public List<Booking> getForBooker(Long userId, String stateName, Pageable pageable) {
        return get(userId, stateName, pageable, true);
    }

    public List<Booking> getForOwner(Long userId, String stateName, Pageable pageable) {
        return get(userId, stateName, pageable, false);
    }

    public static BookingsByStateFarm getFarm(@NonNull BookingRepository repository) {
        return new BookingsByStateFarm(repository);
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