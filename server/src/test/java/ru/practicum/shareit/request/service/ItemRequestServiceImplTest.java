package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;

    private final ItemRequestMapper itemRequestMapper = Mappers.getMapper(ItemRequestMapper.class);

    private ItemRequestService requestService;
    private final UserDto userDto1 = makeUserDto(1L);
    private final User user1 = makeUser(1L);
    private final ItemRequestDto itemRequestDto1 = makeItemRequestDto(1L, userDto1);
    private final ItemRequest itemRequest1 = makeItemRequest(1L, user1);

    @BeforeEach
    void setUp() {
        requestService = new ItemRequestServiceImpl(
                requestRepository,
                userRepository,
                itemRequestMapper
        );
    }

    @Test
    void add_shouldInvokeRepoAndAdded() {
        when(requestRepository.save(any())).thenAnswer(invocationOnMock -> {
            ItemRequest request = invocationOnMock.getArgument(0, ItemRequest.class);
            request.setCreated(null);
            return request;
        });
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        ItemRequestDto added = requestService.add(itemRequestDto1, user1.getId());

        assertEquals(itemRequestDto1, added);
        verify(requestRepository, times(1)).save(itemRequest1);
    }

    @Test
    void findAllByUserId_shouldInvokeRepoAndFoundAsList() {
        final ItemRequest itemRequest2 = makeItemRequest(2L, user1);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(requestRepository.findAllByRequesterId(anyLong())).thenReturn(List.of(itemRequest1, itemRequest2));

        List<ItemRequestDto> found = requestService.findAllByUserId(100L);

        found.forEach(System.out::println);

        assertNotNull(found);
        assertEquals(2, found.size());
        verify(requestRepository, times(1)).findAllByRequesterId(100L);
    }

    @Test
    void findById_shouldInvokeRepoAndFound() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest1));

        ItemRequestDto found = requestService.findById(itemRequest1.getId(), user1.getId());

        assertEquals(itemRequestDto1, found);
        verify(requestRepository, times(1)).findById(1L);
    }

    @Test
    void findById_shouldThrowsException_whenNotExists() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> requestService.findById(itemRequest1.getId(), user1.getId()));

        verifyNoInteractions(requestRepository);
    }

    @Test
    void findAllByPage_shouldInvokeRepoAndFound() {
        final ItemRequest itemRequest2 = makeItemRequest(2L, user1);
        when(requestRepository.findAllByRequesterIdNot(anyLong(), any())).thenReturn(List.of(itemRequest1, itemRequest2));

        List<ItemRequestDto> found = requestService.findAllByPage(user1.getId(), 1, 1);

        assertNotNull(found);
        assertEquals(2, found.size());
        verify(requestRepository, times(1)).findAllByRequesterIdNot(user1.getId(),
                PageRequest.of(1 / 1, 1, Sort.by("created").descending()));
    }

    User makeUser(Long id) {
        return new User(
                id,
                "name_" + id,
                "email_" + id + "@emal.ru"
        );
    }

    private UserDto makeUserDto(long id) {
        return new UserDto(
                id,
                "name_" + id,
                "email_" + id + "@emal.ru"
        );
    }

    private ItemRequestDto makeItemRequestDto(long id, UserDto userDto) {
        return new ItemRequestDto(
                id,
                "description_" + id,
                userDto,
                null,
                null
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