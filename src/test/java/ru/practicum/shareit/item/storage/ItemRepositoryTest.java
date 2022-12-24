package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class ItemRepositoryTest {

    public static final long ID_1 = 1L;
    public static final long ID_2 = 2L;
    public static final long ID_3 = 3L;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id"));
    private User user1;
    private Item item1;
    private Item item2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.save(makeUser(ID_1));

        item1 = itemRepository.save(makeItem(ID_1, user1));
        item2 = itemRepository.save(makeItem(ID_2, user1));
    }

    @AfterEach
    public void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByOwnerId() {
        Page<Item> items = itemRepository.findAllByOwnerId(user1.getId(), pageRequest);

        assertEquals(2, items.getContent().size());
    }

    @Test
    void searchByNameOrDescription() {
        Page<Item> items = itemRepository.searchByNameOrDescription(item1.getName(), pageRequest);

        assertEquals(1, items.getContent().size());
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
}