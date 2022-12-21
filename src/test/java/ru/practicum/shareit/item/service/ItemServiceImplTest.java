package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.enums.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.CommentMapper;
import ru.practicum.shareit.item.comment.storage.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private ItemServiceImpl itemService;

    private User user1;
    private Item item1;
    private Booking booking1;
    private Comment comment1;
    private ItemRequest comment11;
    private ItemDto itemDto1;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemMapper,
                bookingMapper,
                commentMapper
        );

        user1 = makeUser(1L);
        item1 = makeItem(1L, user1);
        booking1 = makeBooking(1L, user1, item1);
        comment1 = makeComment(1L, item1, user1);
        comment11 = makeItemRequest(1L, user1);
        itemDto1 = makeItemDto(1L);
    }


    @Test
    void add_shouldInvokeRepoAndAdded() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(itemRepository.save(any())).thenAnswer(invocationOnMock -> {
            Item item = invocationOnMock.getArgument(0, Item.class);
            item.setId(itemDto1.getId());
            return item;
        });

        ItemDto added = itemService.add(itemDto1, user1.getId());

        assertNotNull(added);
        assertEquals(itemDto1, added);
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void add_shouldThrowExceptionWhenUserIdNotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.add(itemDto1, 100L));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void update_shouldInvokeRepoAndUpdated() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        when(itemRepository.save(any())).thenAnswer(invocationOnMock -> {
            Item item = invocationOnMock.getArgument(0, Item.class);
            item.setId(itemDto1.getId());
            return item;
        });

        ItemDto updated = itemService.update(itemDto1, item1.getId(), user1.getId());

        assertNotNull(updated);
        assertEquals(itemDto1, updated);
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void update_shouldThrowExceptionWhenUserNotOwner() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.update(itemDto1, item1.getId(), 100L));
        assertTrue(exception.getMessage().contains("Only owner can edit"));
    }

    @Test
    void findAllByUserId() {
    }

    @Test
    void findById() {
    }

    @Test
    void searchByNameOrDescription() {
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
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(5),
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