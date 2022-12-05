package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;

import java.util.List;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    @Override
    public BookingDto add(BookingDto bookingDto, Long userId) {
//        bookingDto.setStatus(BookingStatus.WAITING);
//        Booking saved = bookingRepository.save(bookingMapper.toEntity(bookingDto));
//        return bookingMapper.toDto(saved);
        return null;
    }

    @Override
    public BookingDto approve(Long bookingId, Long userId, Boolean approved) {
//        Booking orig = getNonNullObject(bookingRepository, bookingId);
//        if (!userId.equals(orig.getItem().getOwner().getId())) {
//
//        }
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
