package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.model.enums.BookingsByStateFarm.State;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    private final BookingMapper bookingMapper;
    private BookingServiceImpl bookingService;
    private User user1;
    private Item item1;
    private Booking booking1;
    private BookingDtoAdd bookingDtoAdd1;
    private BookingDto bookingDto1;

    @BeforeEach
    void setUp() {
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

        assertThat(added, isNotNull());
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
    void approve_() {
    }

    @Test
    void findById() {
    }

    @ParameterizedTest(name = "{index})  should found by state: {0}")
    @EnumSource(State.class)
    void findAllByBookerId(State state) {
        System.out.println("ok");
    }

    @ParameterizedTest(name = "{index})  should found by state: {0}")
    @EnumSource(State.class)
    void findAllByOwnerId(State state) {
        System.out.println("ok");
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


//    @ParameterizedTest
//    @MethodSource("argsProviderFactory")
//    void testWithMethodSource(String argument) {
//        assertNotNull(argument);
//    }
//
//    static Stream<String> argsProviderFactory() {
//        return Stream.of("alex", "brian");
//    }
}