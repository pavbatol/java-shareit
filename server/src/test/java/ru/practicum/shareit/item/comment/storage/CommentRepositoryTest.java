package ru.practicum.shareit.item.comment.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    public static final long ID_1 = 1L;
    public static final long ID_2 = 2L;
    public static final long ID_3 = 3L;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;

    private final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id"));
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.save(makeUser(ID_1));
        user2 = userRepository.save(makeUser(ID_2));

        item1 = itemRepository.save(makeItem(ID_1, user1));
        item2 = itemRepository.save(makeItem(ID_2, user1));

        commentRepository.save(makeComment(ID_1, item1, user1));
        commentRepository.save(makeComment(ID_2, item1, user2));
        commentRepository.save(makeComment(ID_3, item2, user1));
    }

    @AfterEach
    public void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByItemId() {
        List<Comment> comments = commentRepository.findByItemId(item1.getId());

        assertEquals(2, comments.size());
    }

    @Test
    void findByItemIdIn() {
        List<Comment> comments = commentRepository.findByItemIdIn(List.of(item1.getId(), item2.getId()));

        assertEquals(3, comments.size());
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

    private Comment makeComment(long id, Item item, User user) {
        return new Comment(
                id,
                "text" + id,
                item,
                user,
                LocalDateTime.now()
        );
    }
}