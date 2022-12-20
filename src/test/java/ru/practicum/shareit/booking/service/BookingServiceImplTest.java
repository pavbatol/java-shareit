package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.model.enums.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.model.enums.BookingsByStateFarm.State;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
//    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
//    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
    private BookingServiceImpl bookingService;
    private User user1;
    private Item item1;
    private Booking booking1;


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
    }


    @Test
    void add() {
        BookingDtoAdd bookingDtoAdd1 = new BookingDtoAdd(
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                BookingStatus.WAITING
        );
        BookingDto bookingDto1 = makeBookingDto(1L);

        BookingMapper mockMapper = Mockito.mock(BookingMapper.class);
        BookingServiceImpl bookingService_2 = new BookingServiceImpl(
                bookingRepository,
                mockMapper,
                userRepository,
                itemRepository
        );

//        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
//        when(bookingRepository.save(any())).thenAnswer(returnsFirstArg());

        when(mockMapper.toEntityFilledRelations(any(), any(), any(), any())).thenReturn(booking1);
        when(mockMapper.toDto(any())).thenReturn(bookingDto1);

        BookingDto added = bookingService_2.add(bookingDtoAdd1, 2L);

        assertEquals(bookingDto1, added);
    }


    @Test
    void approve() {
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


    private Item makeItem(long id, User owner) {
        return new Item(
                id,
                "Item_name_" + id,
                "Item_description_" + id,
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

    User makeUser(Long id) {
        return new User(
                id,
                "name_" + id,
                "email_" + id + "@emal.ru"
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