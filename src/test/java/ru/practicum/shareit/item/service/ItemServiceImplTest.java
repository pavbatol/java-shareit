package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import ru.practicum.shareit.item.model.ItemDtoResponse;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    private ItemServiceImpl itemService;

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

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
        verify(itemRepository, times(1)).save(itemArgumentCaptor.capture());
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
        verify(itemRepository, times(1)).save(itemArgumentCaptor.capture());
    }

    @Test
    void update_shouldThrowExceptionWhenUserNotOwner() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.update(itemDto1, item1.getId(), 100L));
        assertTrue(exception.getMessage().contains("Only owner can edit"));
    }

    @Test
    void findAllByUserId_shouldInvokeRepo_andFoundWithComments_andWithCorrectBookings() {
        LocalDateTime now = LocalDateTime.now();

        User user2 = makeUser(2L);

        Item item2 = makeItem(2L, user1);
        Item item3 = makeItem(3L, user2);
        Page<Item> itemPages = new PageImpl<>(List.of(item1, item2, item3));

        Booking bookingLast = makeBooking(2L, user1, item1);
        Booking bookingNext = makeBooking(3L, user1, item1);
        Booking bookingBeforeLast = makeBooking(4L, user1, item1);
        Booking bookingAfterNext = makeBooking(5L, user1, item1);

        bookingLast.setStart(now.minusDays(2));
        bookingLast.setEnd(now.minusDays(1));
        bookingNext.setStart(now.plusDays(1));
        bookingNext.setEnd(now.plusDays(2));

        bookingBeforeLast.setStart(bookingLast.getStart().minusDays(2));
        bookingBeforeLast.setEnd(bookingLast.getEnd().minusDays(1));
        bookingAfterNext.setStart(bookingNext.getStart().plusDays(1));
        bookingAfterNext.setEnd(bookingNext.getEnd().plusDays(2));

        Comment comment2 = makeComment(2L, item1, user1);

        List<Booking> bookings = List.of(bookingBeforeLast, bookingLast, bookingNext, bookingAfterNext);
        List<Comment> comments = List.of(comment1, comment2);

        when(bookingRepository.findByItemIdIn(anyList())).thenReturn(bookings);
        when(commentRepository.findByItemIdIn(anyList())).thenReturn(comments);
        when(itemRepository.findAllByOwnerId(anyLong(), any(Pageable.class))).thenReturn(itemPages);

        List<ItemDtoResponse> found = itemService.findAllByUserId(1L, 1, 1);

        assertNotNull(found);
        assertEquals(3, found.size());
        assertEquals(bookingLast.getId(), found.get(0).getLastBooking().getId());
        assertEquals(bookingNext.getId(), found.get(0).getNextBooking().getId());
        assertNotNull(found.get(0).getComments());
        assertEquals(2, found.get(0).getComments().size());

        verify(bookingRepository, times(1)).findByItemIdIn(anyList());
        verify(commentRepository, times(1)).findByItemIdIn(anyList());
        verify(itemRepository, times(1)).findAllByOwnerId(anyLong(), any(Pageable.class));
    }

    @Test
    void findById_shouldInvokeRepo_andFoundWithComments_andWithCorrectBookings_whenUserIsOwner() {
        LocalDateTime now = LocalDateTime.now();

        Booking bookingLast = makeBooking(2L, user1, item1);
        Booking bookingNext = makeBooking(3L, user1, item1);
        Booking bookingBeforeLast = makeBooking(4L, user1, item1);
        Booking bookingAfterNext = makeBooking(5L, user1, item1);

        bookingLast.setStart(now.minusDays(2));
        bookingLast.setEnd(now.minusDays(1));
        bookingNext.setStart(now.plusDays(1));
        bookingNext.setEnd(now.plusDays(2));

        bookingBeforeLast.setStart(bookingLast.getStart().minusDays(2));
        bookingBeforeLast.setEnd(bookingLast.getEnd().minusDays(1));
        bookingAfterNext.setStart(bookingNext.getStart().plusDays(1));
        bookingAfterNext.setEnd(bookingNext.getEnd().plusDays(2));

        Comment comment2 = makeComment(2L, item1, user1);

        List<Booking> bookings = List.of(bookingBeforeLast, bookingLast, bookingNext, bookingAfterNext);
        List<Comment> comments = List.of(comment1, comment2);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        when(bookingRepository.findByItemId(anyLong())).thenReturn(bookings);
        when(commentRepository.findByItemId(anyLong())).thenReturn(comments);

        ItemDtoResponse found = itemService.findById(1L, 1L);

        assertNotNull(found);
        assertEquals(bookingLast.getId(), found.getLastBooking().getId());
        assertEquals(bookingNext.getId(), found.getNextBooking().getId());
        assertNotNull(found.getComments());
        assertEquals(2, found.getComments().size());

        verify(bookingRepository, times(1)).findByItemId(anyLong());
        verify(commentRepository, times(1)).findByItemId(anyLong());
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void findById_shouldInvokeRepo_andFoundWithComments_andWithCorrectBookings_whenUserNotOwner() {
        LocalDateTime now = LocalDateTime.now();

        Booking bookingLast = makeBooking(2L, user1, item1);
        Booking bookingNext = makeBooking(3L, user1, item1);
        Booking bookingBeforeLast = makeBooking(4L, user1, item1);
        Booking bookingAfterNext = makeBooking(5L, user1, item1);

        bookingLast.setStart(now.minusDays(2));
        bookingLast.setEnd(now.minusDays(1));
        bookingNext.setStart(now.plusDays(1));
        bookingNext.setEnd(now.plusDays(2));

        bookingBeforeLast.setStart(bookingLast.getStart().minusDays(2));
        bookingBeforeLast.setEnd(bookingLast.getEnd().minusDays(1));
        bookingAfterNext.setStart(bookingNext.getStart().plusDays(1));
        bookingAfterNext.setEnd(bookingNext.getEnd().plusDays(2));

        Comment comment2 = makeComment(2L, item1, user1);

        List<Booking> bookings = List.of(bookingBeforeLast, bookingLast, bookingNext, bookingAfterNext);
        List<Comment> comments = List.of(comment1, comment2);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        when(commentRepository.findByItemId(anyLong())).thenReturn(comments);

        long userId = 2;
        if (item1.getOwner().getId() == userId) {
            when(bookingRepository.findByItemId(anyLong())).thenReturn(bookings);
        }

        ItemDtoResponse found = itemService.findById(1L, userId);

        assertNotNull(found);
        assertNull(found.getLastBooking());
        assertNull(found.getNextBooking());
        assertNotNull(found.getComments());
        assertEquals(2, found.getComments().size());

        verify(commentRepository, times(1)).findByItemId(anyLong());
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void searchByNameOrDescription_shouldInvokeRepo_andFoundNotEmptyWhenTextIs() {
        Item item2 = makeItem(2L, user1);
        Page<Item> itemPages = new PageImpl<>(List.of(item1, item2));
        when(itemRepository.searchByNameOrDescription(anyString(), any(Pageable.class))).thenReturn((itemPages));

        List<ItemDto> found = itemService.searchByNameOrDescription("text", 1, 1);

        assertNotNull(found);
        assertEquals(2, found.size());

        verify(itemRepository, times(1)).searchByNameOrDescription(anyString(), any(Pageable.class));
    }

    @Test
    void searchByNameOrDescription_shouldNotInvokeRepo_andFoundEmptyWhenNoText() {
        Item item2 = makeItem(2L, user1);
        Page<Item> itemPages = new PageImpl<>(List.of(item1, item2));

        List<ItemDto> found = itemService.searchByNameOrDescription("", 1, 1);

        assertNotNull(found);
        assertTrue(found.isEmpty());

        verifyNoInteractions(itemRepository);
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