package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.model.CommentDto;
import ru.practicum.shareit.item.comment.model.CommentDtoShort;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    public static final String URL_TEMPLATE = "/items";
    public static final String APPLICATION_JSON = "application/json";
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    public static final long ID_1 = 1L;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;
    @MockBean
    private CommentService commentService;

    private ItemDto itemDto1;
    private ItemDtoResponse itemDtoResponse1;
    private CommentDto commentDto1;
    private CommentDtoShort commentDtoShort1;

    @BeforeEach
    void setUp() {
        itemDto1 = makeItemDto(ID_1);
        itemDtoResponse1 = makeItemDtoResponse(ID_1);
        commentDto1 = makeCommentDto(ID_1);
        commentDtoShort1 = makeCommentDtoShort(ID_1);
    }

    @SneakyThrows
    @Test
    void add_shouldInvokeServiceAndResponseCorrected_whenIncomingDtoIsValid() {
        long userId = ID_1;
        when(itemService.add(itemDto1, userId)).thenReturn(itemDto1);

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto1))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto1)))
                .andReturn().getResponse().getContentAsString();

        verify(itemService, times(1)).add(itemDto1, userId);
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithNameIsNull() {
        ItemDto badIncomingDto = itemDto1.toBuilder().name(null).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badIncomingDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithNameLengthGrater50() {
        ItemDto badIncomingDto = itemDto1.toBuilder().name(new String(new char[51])).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badIncomingDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithDescriptionIsNull() {
        ItemDto badIncomingDto = itemDto1.toBuilder().description(null).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badIncomingDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithDescriptionLengthGrater200() {
        ItemDto badIncomingDto = itemDto1.toBuilder().description(new String(new char[201])).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badIncomingDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithAvailableIsNull() {
        ItemDto badIncomingDto = itemDto1.toBuilder().available(null).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badIncomingDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void update_shouldInvokeServiceAndResponseCorrected_whenIncomingDtoIsValid() {
        long userId = ID_1;
        long itemId = ID_1;
        when(itemService.update(itemDto1, itemId, userId)).thenReturn(itemDto1);

        mockMvc.perform(patch(URL_TEMPLATE + "/{itemId}", itemId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto1))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto1)))
                .andReturn().getResponse().getContentAsString();

        verify(itemService, times(1)).update(itemDto1, itemId, userId);
    }

    @SneakyThrows
    @Test
    void update_shouldBadRequest_whenIncomingDtoWithNameLengthGrater50() {
        ItemDto badIncomingDto = itemDto1.toBuilder().name(String.valueOf(new char[51])).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badIncomingDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void update_shouldBadRequest_whenIncomingDtoWithDescriptionLengthGrater200() {
        ItemDto badIncomingDto = itemDto1.toBuilder().description(String.valueOf(new char[201])).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badIncomingDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void findById_shouldInvokeServiceAndStatusIsOk() {
        long userId = ID_1;
        long itemId = ID_1;
        when(itemService.findById(itemId, userId)).thenReturn(itemDtoResponse1);

        mockMvc.perform(get(URL_TEMPLATE + "/{itemId}", itemId)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDtoResponse1)));

        verify(itemService, times(1)).findById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void findAllByUserId_shouldInvokeServiceAndResponseCorrect_whenParamsIsCorrect() {
        long userId = ID_1;
        int from = 1;
        int size = 1;
        when(itemService.findAllByUserId(userId, from, size)).thenReturn(List.of(itemDtoResponse1, itemDtoResponse1));

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        verify(itemService, times(1)).findAllByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void findAllByUserId_shouldInvokeServiceAndResponseCorrect_whenParamsNotIs() {
        long userId = ID_1;
        int from = 0;
        int size = 10;
        when(itemService.findAllByUserId(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemDtoResponse1, itemDtoResponse1));

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        verify(itemService, times(1)).findAllByUserId(userId, from, size);
    }

    @SneakyThrows
    @Test
    void findAllByUserId_shouldBadRequest_whenFromIsNegative() {
        long userId = ID_1;
        int from = -1;
        int size = 1;

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void findAllByUserId_shouldBadRequest_whenSizeIsZero() {
        long userId = ID_1;
        int from = 1;
        int size = 0;

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void searchByNameOrDescription_shouldInvokeServiceAndResponseCorrect_whenParamsIsCorrect() {
        String text = "text";
        int from = 1;
        int size = 1;

        when(itemService.searchByNameOrDescription(text, from, size)).thenReturn(List.of(itemDto1, itemDto1));

        mockMvc.perform(get(URL_TEMPLATE + "/search")
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        verify(itemService, times(1)).searchByNameOrDescription(text, from, size);
    }


    @SneakyThrows
    @Test
    void searchByNameOrDescription_shouldBadRequest_whenFromIsNegative() {
        String text = "text";
        int from = -1;
        int size = 1;

        mockMvc.perform(get(URL_TEMPLATE + "/search")
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void searchByNameOrDescription_shouldBadRequest_whenSizeIsZero() {
        String text = "text";
        int from = 1;
        int size = 0;

        mockMvc.perform(get(URL_TEMPLATE + "/search")
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(itemService);
    }

    @SneakyThrows
    @Test
    void addComment_shouldInvokeServiceAndResponseCorrected_whenIncomingDtoIsValid() {
        long userId = ID_1;
        long itemId = ID_1;
        when(commentService.add(commentDto1, itemId, userId)).thenReturn(commentDtoShort1);

        mockMvc.perform(post(URL_TEMPLATE + "/{itemId}/comment", itemId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto1))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentDtoShort1)))
                .andReturn().getResponse().getContentAsString();

        verify(commentService, times(1)).add(commentDto1, itemId, userId);
    }

    @SneakyThrows
    @Test
    void addComment_whenIncomingDtoWithTextIsNull() {
        long userId = ID_1;
        long itemId = ID_1;
        CommentDto badCommentDto = commentDto1.toBuilder().text(null).build();

        mockMvc.perform(post(URL_TEMPLATE + "/{itemId}/comment", itemId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badCommentDto))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(commentService);
    }

    @SneakyThrows
    @Test
    void addComment_whenIncomingDtoWithTextIsEmpty() {
        long userId = ID_1;
        long itemId = ID_1;
        CommentDto badCommentDto = commentDto1.toBuilder().text("").build();

        mockMvc.perform(post(URL_TEMPLATE + "/{itemId}/comment", itemId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badCommentDto))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(commentService);
    }

    private ItemDto makeItemDto(long id) {
        return new ItemDto(
                id,
                "name_" + id,
                "description_" + id,
                true,
                null
        );
    }

    private ItemDtoResponse makeItemDtoResponse(long id) {
        return new ItemDtoResponse(
                id,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private CommentDto makeCommentDto(long id) {
        return new CommentDto(
                id,
                "text",
                null,
                null,
                null
        );
    }

    private CommentDtoShort makeCommentDtoShort(long id) {
        return new CommentDtoShort(
                id,
                null,
                null,
                null
        );
    }
}