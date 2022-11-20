package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.db.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.db.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;
import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;
import static ru.practicum.shareit.validator.ValidatorManager.validateId;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    protected static final String ENTITY_SIMPLE_NAME = "Вещь";
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto add(ItemDto itemDto, Long userId) {
        validateId(userStorage, userId);
        Item added = itemStorage.add(toItem(itemDto, userId));
        log.debug("Добавлен {}: {}", ENTITY_SIMPLE_NAME, added);
        return toItemDto(added);
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        Item orig = getNonNullObject(itemStorage, itemId);
        if (!Objects.equals(orig.getOwner(), userId)) {
            throw new NotFoundException("Редактировать может только владелец");
        }
        Item updated = itemStorage.update(toItem(itemDto, orig));
        log.debug("Обновлен {}: {}", ENTITY_SIMPLE_NAME, updated);
        return toItemDto(updated);
    }

    @Override
    public List<ItemDto> findAllByUserId(Long userId) {
        List<Item> found = itemStorage.findAllByUserId(userId);
        log.debug("Текущий размер списка для {}: {}", ENTITY_SIMPLE_NAME, found.size());
        return found.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(Long itemId) {
        Item found = getNonNullObject(itemStorage, itemId);
        log.debug("Найден {}: {}", ENTITY_SIMPLE_NAME, found);
        return toItemDto(found);
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> searched = itemStorage.search(text);
        return null;
    }
}
