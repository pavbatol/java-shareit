package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.BookingDtoAdd;
import ru.practicum.shareit.booking.model.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto add(BookingDtoAdd bookingDto, Long userId);

    BookingDto approve(Long bookingId, Long userId, Boolean approved);

    BookingDto findById(Long bookingId, Long userId);

    List<BookingDto> findAllByBookerId(Long bookerId, String state, int from, int size);

    List<BookingDto> findAllByOwnerId(Long ownerId, String state, int from, int size);
}
