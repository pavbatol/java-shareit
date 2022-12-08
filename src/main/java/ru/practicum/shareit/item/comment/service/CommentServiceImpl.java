package ru.practicum.shareit.item.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.CommentDto;
import ru.practicum.shareit.item.comment.model.CommentMapper;
import ru.practicum.shareit.item.comment.model.CommentShortDto;
import ru.practicum.shareit.item.comment.storage.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentShortDto add(CommentDto dto, Long itemId, Long userId) {
        List<Booking> itemBookings = bookingRepository
                .findByItem_IdAndBooker_IdAndEndIsBeforeAndStatus(
                        itemId, userId, LocalDateTime.now(), BookingStatus.APPROVED
                );
        if (itemBookings.isEmpty()) {
            throw new ValidationException("Only the booker of an approved and ended booking can comment");
        }
        Comment comment = commentMapper.toEntityFilledAuthor(dto, itemId, userId, LocalDateTime.now());
        return commentMapper.toShortDto(commentRepository.save(comment));
    }
}
