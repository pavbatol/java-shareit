package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.storage.BookingRepository;

import java.util.List;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    protected static final String ENTITY_SIMPLE_NAME = "Booking";
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto add(BookingAddDto dto, Long userId) {
        dto.setStatus(BookingStatus.WAITING);
        Booking entity = bookingMapper.toEntity(dto, userId);
        Booking saved = bookingRepository.save(entity);
        log.debug("Added {}: {}", ENTITY_SIMPLE_NAME, saved);
        return bookingMapper.toDto(saved);
    }

    @Override
    public BookingDto approve(Long bookingId, Long userId, Boolean approved) {
        Booking orig = getNonNullObject(bookingRepository, bookingId);
        if (!userId.equals(orig.getItem().getOwner().getId())) {
            throw new RuntimeException("Only owner of item can approve");
        }
//        if (bookingId.get == null) {
//
//        }
        return null;
    }

    @Override
    public BookingDto findById(Long bookingId, Long userId) {
        return null;
    }

    @Override
    public List<BookingDto> findAllByBookerId(Long bookerId, String byState) {
        return null;
    }

    @Override
    public List<BookingDto> findAllByOwnerId(Long ownerId, String byState) {
        return null;
    }
}
