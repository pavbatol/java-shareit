package ru.practicum.shareit.booking.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingDtoAdd;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.enums.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.model.enums.BookingsFactory.State;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    private BookingServiceImpl bookingService;
    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    private User user1;
    private Item item1;
    private Booking booking1;
    private BookingDtoAdd bookingDtoAdd1;
    private BookingDto bookingDto1;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Field field = bookingMapper.getClass().getDeclaredField("userMapper");
        field.setAccessible(true);
        field.set(bookingMapper, userMapper);

        field = bookingMapper.getClass().getDeclaredField("itemMapper");
        field.setAccessible(true);
        field.set(bookingMapper, itemMapper);

        bookingService = new BookingServiceImpl(
                bookingRepository,
                bookingMapper,
                userRepository,
                itemRepository
        );

        user1 = makeUser(1L);
        item1 = makeItem(1L, user1);
        booking1 = makeBooking(1L, user1, item1);

        bookingDtoAdd1 = new BookingDtoAdd(
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                BookingStatus.WAITING
        );

        bookingDto1 = new BookingDto(
                1L,
                makeUserDto(1L),
                makeItemDto(1L),
                bookingDtoAdd1.getStart(),
                bookingDtoAdd1.getEnd(),
                bookingDtoAdd1.getStatus()
        );
    }

    @Test
    void add_shouldInvokeRepoAndAdded() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(bookingRepository.save(any())).thenAnswer(invocationOnMock -> {
            Booking booking = invocationOnMock.getArgument(0, Booking.class);
            booking.setId(bookingDto1.getId());
            return booking;
        });

        BookingDto added = bookingService.add(bookingDtoAdd1, 2L);

        assertNotNull(added);
        assertEquals(bookingDto1, added);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void add_shouldCheckUserNotOwnerIsFailed() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.add(bookingDtoAdd1, 1L));

        assertEquals(exception.getMessage(), "The owner of the item does not need to book it");
    }

    @Test
    void add_shouldCheckAvailableIsFailed() {
        item1.setAvailable(false);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.add(bookingDtoAdd1, 2L));

        assertEquals(exception.getMessage(), "The item is not available for booking");
    }

    @Test
    void add_shouldCheckDatesWhenStartIsPastIsFailed() {
        BookingDtoAdd bookingDtoAdd2 = bookingDtoAdd1.toBuilder()
                .start(LocalDateTime.now().minusHours(1))
                .build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.add(bookingDtoAdd2, 2L));

        assertEquals(exception.getMessage(), "Incorrect booking date-time");
    }

    @Test
    void add_shouldCheckDatesWhenStartIsAfterEndIsFailed() {
        BookingDtoAdd bookingDtoAdd2 = bookingDtoAdd1.toBuilder()
                .start(bookingDtoAdd1.getEnd().plusHours(1))
                .build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.add(bookingDtoAdd2, 2L));

        assertEquals(exception.getMessage(), "Incorrect booking date-time");
    }

    @Test
    void add_shouldCheckDatesWhenStartIsEqualEndIsFailed() {
        BookingDtoAdd bookingDtoAdd2 = bookingDtoAdd1.toBuilder()
                .start(bookingDtoAdd1.getEnd())
                .build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.add(bookingDtoAdd2, 2L));

        assertEquals(exception.getMessage(), "Incorrect booking date-time");
    }

    @Test
    void approve_shouldStatusUpdatedForTrue() {
        boolean approved = true;
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));
        when(bookingRepository.save(any())).thenAnswer(returnsFirstArg());

        BookingDto updated = bookingService.approve(booking1.getId(), user1.getId(), approved);

        assertEquals(BookingStatus.APPROVED, updated.getStatus());
        verify(bookingRepository, times(1)).save(booking1);
    }

    @Test
    void approve_shouldStatusUpdatedForFalse() {
        boolean approved = false;
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));
        when(bookingRepository.save(any())).thenAnswer(returnsFirstArg());

        BookingDto updated = bookingService.approve(booking1.getId(), user1.getId(), approved);

        assertEquals(BookingStatus.REJECTED, updated.getStatus());
        verify(bookingRepository, times(1)).save(booking1);
    }

    @Test
    void approve_shouldThrowsExceptionWhenUserNotOwner() {
        boolean approved = true;
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.approve(booking1.getId(), user1.getId() + 100, approved));

        assertEquals(exception.getMessage(), "Only owner of item can approve");
    }

    @Test
    void approve_shouldThrowsExceptionWhenStatusAlreadySet() {
        boolean approved = true;
        booking1.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.approve(booking1.getId(), user1.getId(), approved));

        assertTrue(exception.getMessage().contains("The status is already set"));
    }

    @Test
    void findById_shouldInvokeRepoAndFound() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));

        BookingDto found = bookingService.findById(booking1.getId(), user1.getId());

        assertEquals(bookingMapper.toDto(booking1), found);
        verify(bookingRepository, times(1)).findById(booking1.getId());
    }

    @Test
    void findById_shouldThrowsExceptionWhenUserNotOwnerAndNotBooker() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.findById(booking1.getId(), user1.getId() + 100));

        assertEquals(exception.getMessage(), "Only the item owner or the booker can view");
    }

    @ParameterizedTest(name = "{index}) should found by state: {0}")
    @EnumSource(State.class)
    void findAllByBookerId_shouldInvokeRepo_andInvokedMethodDependsOnStateByFactory(State state) {
        int from = 1;
        int size = 10;

        Booking booking2 = makeBooking(2, user1, item1);
        Page<Booking> page = new PageImpl<>(List.of(booking1, booking2));

        Executor finder = () -> {
            List<BookingDto> found = bookingService.findAllByBookerId(1L, state.name(), from, size);
            assertNotNull(found);
            assertEquals(2, found.size());
        };

        switch (state) {
            case ALL:
                when(bookingRepository.findAllByBookerId(anyLong(), any(Pageable.class))).thenReturn(page);
                finder.execute();
                verify(bookingRepository, times(1))
                        .findAllByBookerId(anyLong(), any(Pageable.class));
                break;
            case WAITING:
            case REJECTED:
                when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(BookingStatus.class),
                        any(Pageable.class))).thenReturn(page);
                finder.execute();
                verify(bookingRepository, times(1)).findAllByBookerIdAndStatus(anyLong(),
                        any(BookingStatus.class), any(Pageable.class));
                break;
            case PAST:
                when(bookingRepository.findAllByBookerIdAndEndBefore(anyLong(), any(LocalDateTime.class),
                        any(Pageable.class))).thenReturn(page);
                finder.execute();
                verify(bookingRepository, times(1))
                        .findAllByBookerIdAndEndBefore(anyLong(), any(LocalDateTime.class), any(Pageable.class));
                break;
            case CURRENT:
                when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                        any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);
                finder.execute();
                verify(bookingRepository, times(1))
                        .findAllByBookerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                                any(LocalDateTime.class), any(Pageable.class));
                break;
            case FUTURE:
                when(bookingRepository.findAllByBookerIdAndStartAfter(anyLong(), any(LocalDateTime.class),
                        any(Pageable.class))).thenReturn(page);
                finder.execute();
                verify(bookingRepository, times(1))
                        .findAllByBookerIdAndStartAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class));
                break;
        }
    }

    @Test
    void findAllByBookerId_shouldThrowsExceptionWhenFoundIsEmpty() {
        when(bookingRepository.findAllByBookerId(anyLong(), any(Pageable.class))).thenReturn(Page.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.findAllByBookerId(1L, State.ALL.name(), 1, 1));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @ParameterizedTest(name = "{index})  should found by state: {0}")
    @EnumSource(State.class)
    void findAllByOwnerId_shouldInvokeRepo_andInvokedMethodDependsOnStateByFactory(State state) {
        int from = 1;
        int size = 10;

        Booking booking2 = makeBooking(2, user1, item1);
        Page<Booking> page = new PageImpl<>(List.of(booking1, booking2));

        Executor finder = () -> {
            List<BookingDto> found = bookingService.findAllByOwnerId(1L, state.name(), from, size);
            assertNotNull(found);
            assertEquals(2, found.size());
        };

        switch (state) {
            case ALL:
                when(bookingRepository.findAllByItemOwnerId(anyLong(), any(Pageable.class))).thenReturn(page);
                finder.execute();
                verify(bookingRepository, times(1))
                        .findAllByItemOwnerId(anyLong(), any(Pageable.class));
                break;
            case WAITING:
            case REJECTED:
                when(bookingRepository.findAllByItemOwnerIdAndStatus(anyLong(), any(BookingStatus.class),
                        any(Pageable.class))).thenReturn(page);
                finder.execute();
                verify(bookingRepository, times(1)).findAllByItemOwnerIdAndStatus(anyLong(),
                        any(BookingStatus.class), any(Pageable.class));
                break;
            case PAST:
                when(bookingRepository.findAllByItemOwnerIdAndEndBefore(anyLong(), any(LocalDateTime.class),
                        any(Pageable.class))).thenReturn(page);
                finder.execute();
                verify(bookingRepository, times(1))
                        .findAllByItemOwnerIdAndEndBefore(anyLong(), any(LocalDateTime.class), any(Pageable.class));
                break;
            case CURRENT:
                when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                        any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);
                finder.execute();
                verify(bookingRepository, times(1))
                        .findAllByItemOwnerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                                any(LocalDateTime.class), any(Pageable.class));
                break;
            case FUTURE:
                when(bookingRepository.findAllByItemOwnerIdAndStartAfter(anyLong(), any(LocalDateTime.class),
                        any(Pageable.class))).thenReturn(page);
                finder.execute();
                verify(bookingRepository, times(1))
                        .findAllByItemOwnerIdAndStartAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class));
                break;
        }
    }

    @Test
    void findAllByOwnerId_shouldThrowsExceptionWhenFoundIsEmpty() {
        when(bookingRepository.findAllByItemOwnerId(anyLong(), any(Pageable.class))).thenReturn(Page.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.findAllByOwnerId(1L, State.ALL.name(), 1, 1));
        assertTrue(exception.getMessage().contains("not found"));
    }

    private BookingDtoAdd makeBookingDtoAdd(long id) {
        return new BookingDtoAdd(
                id,
                LocalDateTime.now(),
                LocalDateTime.now(),
                BookingStatus.WAITING
        );
    }

    private BookingDto makeBookingDto(long id) {
        return new BookingDto(
                id,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                BookingStatus.WAITING
        );
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

    @FunctionalInterface
    interface Executor {
        void execute();
    }
}
