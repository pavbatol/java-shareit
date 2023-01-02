package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookingRepositoryTest {

    public static final long ID_1 = 1L;
    public static final long ID_2 = 2L;
    public static final long ID_3 = 3L;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
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

    @BeforeEach
    public void setUp() {
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
    }

    @AfterEach
    public void tearDown() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByBookerId() {
        Page<Booking> bookings = bookingRepository.findAllByBookerId(user1.getId(), pageRequest);

        assertEquals(2, bookings.getContent().size());
    }

    @Test
    void findAllByBookerIdAndStatus() {
        booking1.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking1);

        Page<Booking> bookings = bookingRepository.findAllByBookerIdAndStatus(user1.getId(), BookingStatus.APPROVED, pageRequest);

        assertEquals(1, bookings.getContent().size());
    }

    @Test
    void findAllByBookerIdAndEndBefore() {
        LocalDateTime start = now.minusDays(10);
        LocalDateTime end = now.minusDays(9);
        booking1.setStart(start);
        booking1.setEnd(end);
        bookingRepository.save(booking1);

        Page<Booking> bookings = bookingRepository.findAllByBookerIdAndEndBefore(user1.getId(), end.plusDays(1), pageRequest);

        assertEquals(1, bookings.getContent().size());
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfter() {
        LocalDateTime start = now.minusDays(1);
        LocalDateTime end = now.plusDays(1);
        booking1.setStart(start);
        booking1.setEnd(end);
        bookingRepository.save(booking1);

        Page<Booking> bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(user1.getId(), now, now, pageRequest);

        assertEquals(1, bookings.getContent().size());
    }

    @Test
    void findAllByBookerIdAndStartAfter() {
        Page<Booking> bookings = bookingRepository.findAllByBookerIdAndStartAfter(user1.getId(), now, pageRequest);

        assertEquals(2, bookings.getContent().size());
    }

    @Test
    void findAllByItemOwnerId() {
        Page<Booking> bookings = bookingRepository.findAllByItemOwnerId(user1.getId(), pageRequest);

        assertEquals(3, bookings.getContent().size());
    }

    @Test
    void findAllByItemOwnerIdAndStatus() {
        Page<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStatus(user1.getId(), BookingStatus.WAITING,
                pageRequest);

        assertEquals(3, bookings.getContent().size());
    }

    @Test
    void findAllByItemOwnerIdAndEndBefore() {
        LocalDateTime start = now.minusDays(10);
        LocalDateTime end = now.minusDays(9);
        booking1.setStart(start);
        booking1.setEnd(end);
        bookingRepository.save(booking1);

        Page<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndEndBefore(user1.getId(), end.plusDays(1), pageRequest);

        assertEquals(1, bookings.getContent().size());
    }

    @Test
    void findAllByItemOwnerIdAndStartBeforeAndEndAfter() {
        LocalDateTime start = now.minusDays(1);
        LocalDateTime end = now.plusDays(1);
        booking1.setStart(start);
        booking1.setEnd(end);
        bookingRepository.save(booking1);

        Page<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(user1.getId(), now, now, pageRequest);

        assertEquals(1, bookings.getContent().size());
    }

    @Test
    void findAllByItemOwnerIdAndStartAfter() {
        Page<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStartAfter(user1.getId(), now, pageRequest);

        assertEquals(3, bookings.getContent().size());
    }

    @Test
    void findByItemId() {
        List<Booking> bookings = bookingRepository.findByItemId(item1.getId());

        assertEquals(1, bookings.size());
    }

    @Test
    void findByItemIdIn() {
        List<Booking> bookings = bookingRepository.findByItemIdIn(List.of(item1.getId(), item2.getId()));

        assertEquals(3, bookings.size());
    }

    @Test
    void existsByItemIdAndBookerIdAndEndBeforeAndStatus() {
        boolean exists = bookingRepository.existsByItemIdAndBookerIdAndEndBeforeAndStatus(item1.getId(), user2.getId(),
                now.plusDays(100), BookingStatus.WAITING);

        assertTrue(exists);
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