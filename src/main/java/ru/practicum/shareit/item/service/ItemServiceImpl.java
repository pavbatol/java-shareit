package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    protected static final String ENTITY_SIMPLE_NAME = "Thing";
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemMapper mapper;

    @Override
    public ItemDto add(ItemDto itemDto, Long userId) {
        Item added = itemStorage.add(mapper.toEntity(itemDto, getNonNullObject(userStorage, userId)));
        log.debug("Added {}: {}", ENTITY_SIMPLE_NAME, added);
        return mapper.toDto(added);
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        Item orig = getNonNullObject(itemStorage, itemId);
        if (!Objects.equals(orig.getOwner().getId(), userId)) {
            throw new NotFoundException("Only owner can edit");
        }
        Item updated = itemStorage.update(mapper.updateEntity(itemDto, orig));
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);
        return mapper.toDto(updated);
    }

    @Override
    public List<ItemDto> findAllByUserId(Long userId) {
        List<Item> found = itemStorage.findAllByUserId(userId);
        log.debug("The current size of the list for {}: {}", ENTITY_SIMPLE_NAME, found.size());
        return mapper.toDtos(found);
    }

    @Override
    public ItemDto findById(Long itemId) {
        Item found = getNonNullObject(itemStorage, itemId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);
        return mapper.toDto(found);
    }

    @Override
    public List<ItemDto> searchByNameOrDescription(String text) {
        List<Item> searched = StringUtils.isBlank(text)
                ? Collections.emptyList()
                : itemStorage.searchByNameOrDescription(text);
        return mapper.toDtos(searched);
    }
}
