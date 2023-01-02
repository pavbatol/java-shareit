package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBookerId(Long userId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatus(Long userId, BookingStatus status, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBefore(Long userId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartAfter(Long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItemOwnerId(Long ownerId, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndEndBefore(Long ownerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartAfter(Long ownerId, LocalDateTime start, Pageable pageable);

    List<Booking> findByItemId(Long itemId);

    List<Booking> findByItemIdIn(List<Long> itemIds);

    boolean existsByItemIdAndBookerIdAndEndBeforeAndStatus(Long itemId, Long userId, LocalDateTime end, BookingStatus status);
}
