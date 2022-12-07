package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long userId);

    List<Booking> findAllByBooker_IdAndStatus(Long userId, BookingStatus status);

    List<Booking> findAllByBooker_IdAndEndIsBefore(Long userId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartIsAfter(Long userId, LocalDateTime start);

    List<Booking> findAllByItem_Owner_Id(Long ownerId);

    List<Booking> findAllByItem_Owner_IdAndStatus(Long ownerId, BookingStatus status);

    List<Booking> findAllByItem_Owner_IdAndEndIsBefore(Long ownerId, LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndStartIsAfter(Long ownerId, LocalDateTime start);

}
