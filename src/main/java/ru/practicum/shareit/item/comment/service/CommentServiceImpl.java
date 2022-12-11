package ru.practicum.shareit.item.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.enums.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.CommentDto;
import ru.practicum.shareit.item.comment.model.CommentDtoShort;
import ru.practicum.shareit.item.comment.model.CommentMapper;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    @Override
    public CommentDtoShort add(CommentDto dto, Long itemId, Long userId) {
        if (!bookingRepository.existsByItemIdAndBookerIdAndEndBeforeAndStatus(
                itemId, userId, LocalDateTime.now(), BookingStatus.APPROVED)) {
            throw new ValidationException("Only the booker of an approved and ended booking can comment");
        }
        Comment comment = commentMapper.toEntityFilledAuthor(dto, itemId, userId, LocalDateTime.now(), userRepository);
        return commentMapper.toShortDto(commentRepository.save(comment));
    }
}
