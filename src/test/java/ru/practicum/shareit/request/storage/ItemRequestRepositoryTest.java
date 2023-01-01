package ru.practicum.shareit.request.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {

    public static final long ID_1 = 1L;
    public static final long ID_2 = 2L;
    public static final long ID_3 = 3L;
    public static final LocalDateTime NOW = LocalDateTime.now();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("created").descending());
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;
    private ItemRequest itemRequest3;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.save(makeUser(ID_1));
        user2 = userRepository.save(makeUser(ID_2));

        itemRequest1 = makeItemRequest(ID_1, user1);
        itemRequest2 = makeItemRequest(ID_2, user1);
        itemRequest3 = makeItemRequest(ID_3, user2);
        itemRequest1.setCreated(NOW);
        itemRequest2.setCreated(NOW);
        itemRequest3.setCreated(NOW);

        itemRequest1 = itemRequestRepository.save(itemRequest1);
        itemRequest2 = itemRequestRepository.save(itemRequest2);
        itemRequest3 = itemRequestRepository.save(itemRequest3);

        item1 = makeItem(ID_1, user1);
        item2 = makeItem(ID_2, user1);
        item1.setRequest(itemRequest1);
        item2.setRequest(itemRequest1);
        item1 = itemRepository.save(item1);
        item2 = itemRepository.save(item2);
    }

    @AfterEach
    public void tearDown() {
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByRequesterId() {
        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterId(user1.getId());

        assertEquals(2, requests.size());
    }

    @Test
    void findAllByRequesterIdNot() {
        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdNot(user1.getId(), pageRequest);

        assertEquals(1, requests.size());
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