package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.BookingAddDto;
import ru.practicum.shareit.booking.model.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto add(BookingAddDto bookingDto, Long userId);

    BookingDto approve(Long bookingId, Long userId, Boolean approved);

    BookingDto findById(Long bookingId, Long userId);

    List<BookingDto> findAllByBookerId(Long bookerId, String byState);

    List<BookingDto> findAllByOwnerId(Long ownerId, String byState);
}
