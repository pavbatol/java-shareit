package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.validator.ValidatorManager.checkId;
import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    protected static final String ENTITY_SIMPLE_NAME = "Request";
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDto add(ItemRequestDto dto, Long userId) {
        ItemRequest itemRequest = itemRequestMapper.toEntity(dto,
                getNonNullObject(userRepository, userId),
                LocalDateTime.now());
        ItemRequest saved = itemRequestRepository.save(itemRequest);
        log.debug("Created ItemRequest: {}", saved);
        return itemRequestMapper.toDto(saved);
    }

    @Override
    public List<ItemRequestDto> findAllByUserId(Long userId) {
        checkId(userRepository, userId);
        List<ItemRequest> found = itemRequestRepository.findAllByRequesterId(userId);
        log.debug("The current size of the list for {}: {} for userId #{}", ENTITY_SIMPLE_NAME, found.size(), userId);
        return itemRequestMapper.toDtos(found);
    }

    @Override
    public ItemRequestDto findById(Long requestId, Long userId) {
        checkId(userRepository, userId);
        ItemRequest found = getNonNullObject(itemRequestRepository, requestId);
        log.debug("Found {} for id #{}: {}", ENTITY_SIMPLE_NAME, requestId, found);
        return itemRequestMapper.toDto(found);
    }


}
