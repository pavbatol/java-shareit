package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {


    public static final long ID_1 = 1L;
    public static final long ID_2 = 2L;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private User user1;
    private Item item1;
    private Item item2;

    @Test
    void findAllByUserId() {
        user1 = userRepository.save(makeUser(ID_1));
        item1 = itemRepository.save(makeItem(ID_1, user1));
        item2 = itemRepository.save(makeItem(ID_2, user1));

        List<ItemDtoResponse> itemDtoResponses = itemService.findAllByUserId(user1.getId(), 1, 10);

        assertEquals(2, itemDtoResponses.size());
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