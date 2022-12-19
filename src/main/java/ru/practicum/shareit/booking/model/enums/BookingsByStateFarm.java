package ru.practicum.shareit.booking.model.enums;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.IllegalEnumException;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    public List<Booking> get(Long userId,
                             State state,
                             @NotNull Pageable pageable,
                             boolean isBooker) {
        return isBooker
                ? bookerFunctions.get(state).apply(userId, pageable).getContent()
                : ownerFunctions.get(state).apply(userId, pageable).getContent();
    }

    public List<Booking> get(Long userId,
                             @NotNull String stateName,
                             @NotNull Pageable pageable,
                             boolean isBooker) {
        State state;
        try {
            state = State.valueOf(stateName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalEnumException("Unknown state: " + stateName);
        }
        return get(userId, state, pageable, isBooker);
    }

    public static BookingsByStateFarm getFarm(@NotNull BookingRepository repository) {
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