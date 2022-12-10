package ru.practicum.shareit.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Component
@RequiredArgsConstructor
public class GroupService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public User getUser(Long id) {
        return getNonNullObject(userRepository, id);
    }

    public Item getItem(Long id) {
        return getNonNullObject(itemRepository, id);
    }
}
