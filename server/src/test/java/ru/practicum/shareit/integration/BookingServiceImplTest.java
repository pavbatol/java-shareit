package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookingServiceImplTest {

    public static final long ID_1 = 1L;
    public static final long ID_2 = 2L;
    public static final long ID_3 = 3L;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private User user1;
    private User user2;
    private User user3;
    private Item item1;
    private Item item2;
    private Item item3;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private LocalDateTime now;

    @Test
    void findAllByBookerId() {
        now = LocalDateTime.now();

        user1 = userRepository.save(makeUser(ID_1));
        user2 = userRepository.save(makeUser(ID_2));
        user3 = userRepository.save(makeUser(ID_3));

        item1 = itemRepository.save(makeItem(ID_1, user1));
        item2 = itemRepository.save(makeItem(ID_2, user1));
        item3 = itemRepository.save(makeItem(ID_3, user2));

        booking1 = bookingRepository.save(makeBooking(ID_1, user1, item2));
        booking2 = bookingRepository.save(makeBooking(ID_2, user1, item2));
        booking3 = bookingRepository.save(makeBooking(ID_3, user2, item1));

        List<BookingDto> bookingDtos = bookingService.findAllByBookerId(user1.getId(), "ALL", 1, 10);

        assertEquals(2, bookingDtos.size());
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
                now.plusDays(1),
                now.plusDays(5),
                BookingStatus.WAITING
        );
    }
}