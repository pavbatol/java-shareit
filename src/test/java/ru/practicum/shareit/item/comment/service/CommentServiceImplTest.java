package ru.practicum.shareit.item.comment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enums.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.CommentDto;
import ru.practicum.shareit.item.comment.model.CommentDtoShort;
import ru.practicum.shareit.item.comment.model.CommentMapper;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    private CommentServiceImpl commentService;
    private User user1;
    private Item item1;
    private Comment comment1;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceImpl(
                commentRepository,
                bookingRepository,
                commentMapper,
                userRepository
        );

        user1 = makeUser(1L);
        item1 = makeItem(1L, user1);
        comment1 = makeComment(1L, item1, user1);
    }

    @Test
    void add_shouldInvokeRepoAndAdded() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment1);
        when(bookingRepository.existsByItemIdAndBookerIdAndEndBeforeAndStatus(
                anyLong(), anyLong(), any(LocalDateTime.class), any(BookingStatus.class)))
                .thenReturn(true);

        CommentDto commentDto = makeCommentDto(1L);

        CommentDtoShort added = commentService.add(commentDto, 1L, 1L);

        assertNotNull(added);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void add_shouldThrowsExceptionWhenConditionIsFalse() {
        when(bookingRepository.existsByItemIdAndBookerIdAndEndBeforeAndStatus(
                anyLong(), anyLong(), any(LocalDateTime.class), any(BookingStatus.class)))
                .thenReturn(false);

        CommentDto commentDto = makeCommentDto(1L);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> commentService.add(commentDto, 1L, 1L));
        assertEquals(exception.getMessage(), "Only the booker of an approved and ended booking can comment");
    }

    private UserDto makeUserDto(long id) {
        return new UserDto(
                id,
                "name_" + id,
                "email_" + id + "@emal.ru"
        );
    }

    private ItemDto makeItemDto(long id) {
        return new ItemDto(
                id,
                "name_" + id,
                "description_" + id,
                true,
                null
        );
    }

    User makeUser(Long id) {
        return new User(
                id,
                "name_" + id,
                "email_" + id + "@emal.ru"
        );
    }


    private Item makeItem(long id, User owner) {
        return new Item(
                id,
                "name_" + id,
                "description_" + id,
                true,
                null,
                owner
        );
    }

    private Booking makeBooking(long id, User user, Item item) {
        return new Booking(
                id,
                user,
                item,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(5),
                BookingStatus.WAITING
        );
    }

    private Comment makeComment(long id, Item item, User user) {
        return new Comment(
                id,
                "text" + id,
                item,
                user,
                null
        );
    }

    private CommentDto makeCommentDto(long id) {
        return new CommentDto(
                id,
                "text_" + id,
                null,
                null,
                LocalDateTime.now()
        );
    }

    private ItemRequestDto makeItemRequestDto(long id, UserDto userDto) {
        return new ItemRequestDto(
                id,
                "description_" + id,
                userDto,
                null,
                null
        );
    }

    private ItemRequest makeItemRequest(long id, User user) {
        return new ItemRequest(
                id,
                "description_" + id,
                user,
                null,
                null
        );
    }
}