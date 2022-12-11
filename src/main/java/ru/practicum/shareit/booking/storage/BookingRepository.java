package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long userId);

    List<Booking> findAllByBookerIdAndStatus(Long userId, BookingStatus status);

    List<Booking> findAllByBookerIdAndEndBefore(Long userId, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartAfter(Long userId, LocalDateTime start);

    List<Booking> findAllByItemOwnerId(Long ownerId);

    List<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndEndBefore(Long ownerId, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndStartAfter(Long ownerId, LocalDateTime start);

    List<Booking> findByItemId(Long itemId);

    List<Booking> findByItemIdIn(List<Long> itemIds);

    boolean existsByItemIdAndBookerIdAndEndBeforeAndStatus(Long itemId, Long userId, LocalDateTime end, BookingStatus status);
}
