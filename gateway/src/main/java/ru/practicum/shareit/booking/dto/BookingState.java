package ru.practicum.shareit.booking.dto;

import java.util.Arrays;
import java.util.Optional;

public enum BookingState {
    ALL,
    CURRENT,
    FUTURE,
    PAST,
    REJECTED,
    WAITING;

    public static Optional<BookingState> from(String stateName) {
        return Arrays.stream(values())
                .filter(state -> state.name().equalsIgnoreCase(stateName))
                .findFirst();
    }
}