package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDtoAdd;
import ru.practicum.shareit.item.dto.ItemDtoAdd;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    private static final String URL_TEMPLATE = "/items";
    private static final String APPLICATION_JSON = "application/json";
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private static final long ID_1 = 1L;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private ItemClient client;

    @SneakyThrows
    @Test
    void add_shouldStatusOk_whenInputDtoIsValid() {
        final long userId = ID_1;
        final ItemDtoAdd itemDtoAdd = makeItemDtoAdd(ID_1);

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDtoAdd))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk());

        verify(client, times(1)).add(itemDtoAdd, userId);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeBadDtoAdd")
    void add_shouldStatusBad_whenInputDtoIsNotValid(String name, ItemDtoAdd itemDtoAdd) {
        final long userId = ID_1;

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDtoAdd))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeValidDtoUpdate")
    void update_shouldStatusOk_whenInputDtoIsValid(String name, ItemDtoUpdate itemDtoUpdate) {
        final long userId = ID_1;
        final long itemId = ID_1;

        mockMvc.perform(patch(URL_TEMPLATE + "/{itemId}", itemId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDtoUpdate))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk());

        verify(client, times(1)).update(itemDtoUpdate, itemId, userId);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeBadDtoUpdate")
    void update_shouldStatusBad_whenInputDtoIsNotValid(String name, ItemDtoUpdate itemDtoUpdate) {
        final long userId = ID_1;
        final long itemId = ID_1;

        mockMvc.perform(patch(URL_TEMPLATE + "/{itemId}", itemId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDtoUpdate))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @SneakyThrows
    @Test
    void findById_shouldInvokeClient() {
        final long userId = ID_1;
        final long itemId = ID_1;

        mockMvc.perform(get(URL_TEMPLATE + "/{itemId}", itemId)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk());

        verify(client, times(1)).findById(itemId, userId);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeFromAndSize")
    void findAllByUserId_shouldInvokeOrNotClient_whenValidOrNotData(String name,
                                                                    Integer from,
                                                                    Integer size,
                                                                    HttpStatus httpStatus) {
        final long userId = ID_1;

        mockMvc.perform(get(URL_TEMPLATE + "?from={from}&size={size}", from, size)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().is(httpStatus.value()));

        if (httpStatus.is2xxSuccessful()) {
            verify(client, times(1))
                    .findAllByUserId(
                            userId,
                            from != null ? from : 0,
                            size != null ? size : 10);
        } else {
            verifyNoInteractions(client);
        }
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeFromAndSize")
    void searchByNameOrDescription_shouldInvokeOrNotClient_whenValidOrNotData(String name,
                                                                              Integer from,
                                                                              Integer size,
                                                                              HttpStatus httpStatus) {
        final long userId = ID_1;
        final String text = "any text";

        mockMvc.perform(get(URL_TEMPLATE + "/search?text={text}&from={from}&size={size}", text, from, size)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().is(httpStatus.value()));

        if (httpStatus.is2xxSuccessful()) {
            verify(client, times(1))
                    .searchByNameOrDescription(
                            text,
                            from != null ? from : 0,
                            size != null ? size : 10);
        } else {
            verifyNoInteractions(client);
        }
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeBadAndValidCommentDtoAdd")
    void addComment_shouldInvokeOrNotClient_whenValidOrNotData(String name,
                                                               CommentDtoAdd commentDtoAdd,
                                                               HttpStatus httpStatus) {
        final long userId = ID_1;
        final long itemId = ID_1;

        mockMvc.perform(post(URL_TEMPLATE + "/{itemId}/comment", itemId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDtoAdd))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().is(httpStatus.value()));


        if (httpStatus.is2xxSuccessful()) {
            verify(client, times(1)).addComment(commentDtoAdd, itemId, userId);
        } else {
            verifyNoInteractions(client);
        }
    }

    private static ItemDtoAdd makeItemDtoAdd(long id) {
        return new ItemDtoAdd(
                id,
                "itemName_" + id,
                "itemDescription_" + id,
                true,
                null
        );
    }

    private static ItemDtoUpdate makeItemDtoUpdate(long id) {
        return new ItemDtoUpdate(
                id,
                "itemName_" + id,
                "itemDescription_" + id,
                true,
                null
        );
    }

    private static CommentDtoAdd makeCommentDtoAdd(long id) {
        return new CommentDtoAdd(
                id,
                "commentText_" + id,
                null,
                null,
                null
        );
    }

    public static Stream<Arguments> makeBadDtoAdd() {
        ItemDtoAdd itemDtoAdd = makeItemDtoAdd(ID_1);
        return Stream.of(
                Arguments.of("name_is_empty", itemDtoAdd.toBuilder().name("").build()),
                Arguments.of("name_is_blank", itemDtoAdd.toBuilder().name(" ").build()),
                Arguments.of("name_is_longer_50", itemDtoAdd.toBuilder()
                        .name(String.format("%051d", 1)).build()),
                Arguments.of("name_is_null", itemDtoAdd.toBuilder().name(null).build()),

                Arguments.of("description_is_empty", itemDtoAdd.toBuilder().description("").build()),
                Arguments.of("description_is_blank", itemDtoAdd.toBuilder().description(" ").build()),
                Arguments.of("description_is_longer_200", itemDtoAdd.toBuilder()
                        .description(String.format("%201c", 1)).build()),
                Arguments.of("description_is_null", itemDtoAdd.toBuilder().description(null).build()),

                Arguments.of("available_is_null", itemDtoAdd.toBuilder().available(null).build())
        );
    }

    public static Stream<Arguments> makeBadDtoUpdate() {
        ItemDtoUpdate itemDtoUpdate = makeItemDtoUpdate(ID_1);
        return Stream.of(
                Arguments.of("name_is_empty", itemDtoUpdate.toBuilder().name("").build()),
                Arguments.of("name_is_blank", itemDtoUpdate.toBuilder().name(" ").build()),
                Arguments.of("name_is_longer_50", itemDtoUpdate.toBuilder().name("a".repeat(51)).build()),

                Arguments.of("description_is_empty", itemDtoUpdate.toBuilder().description("").build()),
                Arguments.of("description_is_blank", itemDtoUpdate.toBuilder().description(" ").build()),
                Arguments.of("description_is_longer_200", itemDtoUpdate.toBuilder()
                        .description("a".repeat(201)).build())
        );
    }

    public static Stream<Arguments> makeValidDtoUpdate() {
        ItemDtoUpdate itemDtoUpdate = makeItemDtoUpdate(ID_1);
        return Stream.of(
                Arguments.of("all_is_valid", itemDtoUpdate),
                Arguments.of("name_is_null", itemDtoUpdate.toBuilder().name(null).build()),
                Arguments.of("description_is_null", itemDtoUpdate.toBuilder().description(null).build()),
                Arguments.of("available_is_null", itemDtoUpdate.toBuilder().available(null).build())
        );
    }

    public static Stream<Arguments> makeFromAndSize() {
        return Stream.of(
                Arguments.of("both_is_valid", 0, 10, HttpStatus.OK),
                Arguments.of("both_is_valid", null, null, HttpStatus.OK),
                Arguments.of("from_is_negative", -1, 10, HttpStatus.BAD_REQUEST),
                Arguments.of("size_is_zero", 1, 0, HttpStatus.BAD_REQUEST)
        );
    }

    public static Stream<Arguments> makeBadAndValidCommentDtoAdd() {
        CommentDtoAdd commentDtoAdd = makeCommentDtoAdd(ID_1);
        return Stream.of(
                Arguments.of("text_is_valid", commentDtoAdd, HttpStatus.OK),
                Arguments.of("text_is_empty", commentDtoAdd.toBuilder().text("").build(), HttpStatus.BAD_REQUEST),
                Arguments.of("text_is_blank", commentDtoAdd.toBuilder().text(" ").build(), HttpStatus.BAD_REQUEST),
                Arguments.of("text_is_null", commentDtoAdd.toBuilder().text(null).build(), HttpStatus.BAD_REQUEST)
        );
    }
}