package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDtoAdd;

import java.util.stream.Stream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    private static final String URL_TEMPLATE = "/requests";
    private static final String APPLICATION_JSON = "application/json";
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private static final long ID_1 = 1L;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private ItemRequestClient client;

    @SneakyThrows
    @Test
    void add_shouldStatusOk_whenInputDtoIsValid() {
        final long userId = ID_1;
        ItemRequestDtoAdd dtoAdd = makeItemRequestDtoAdd(ID_1);

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAdd))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk());

        verify(client).add(dtoAdd, userId);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeBadDtoAdd")
    void add_shouldStatusBad_whenInputDtoIsNotValid(String name, ItemRequestDtoAdd dtoAdd) {
        final long userId = ID_1;

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAdd))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @SneakyThrows
    @Test
    void findAllByUserId_() {
        final long userId = ID_1;

        mockMvc.perform(get(URL_TEMPLATE)
                .header(X_SHARER_USER_ID, userId)
        ).andExpect(status().isOk());

        verify(client).findAllByUserId(userId);
    }

    @SneakyThrows
    @Test
    void findById_() {
        final long userId = ID_1;
        final long requestId = ID_1;

        mockMvc.perform(get(URL_TEMPLATE + "/{requestId}", requestId)
                .header(X_SHARER_USER_ID, userId)
        ).andExpect(status().isOk());

        verify(client).findById(requestId, userId);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeFromAndSize")
    void findAllByPage_(String name,
                        Integer from,
                        Integer size,
                        HttpStatus httpStatus) {
        final long userId = ID_1;

        mockMvc.perform(get(URL_TEMPLATE + "/all?from={from}&size={size}", from, size)
                .header(X_SHARER_USER_ID, userId)
        ).andExpect(status().is(httpStatus.value()));

        if (httpStatus.is2xxSuccessful()) {
            verify(client).findAllByPage(
                    userId,
                    from != null ? from : 0,
                    size != null ? size : 10
            );
        } else {
            verifyNoInteractions(client);
        }
    }

    private static ItemRequestDtoAdd makeItemRequestDtoAdd(Long id) {
        return new ItemRequestDtoAdd(
                id,
                "descriptionRequest_" + id
        );
    }

    public static Stream<Arguments> makeBadDtoAdd() {
        ItemRequestDtoAdd dto = makeItemRequestDtoAdd(ID_1);
        return Stream.of(
                Arguments.of("description_is_blank", dto.toBuilder().description(" ").build()),
                Arguments.of("description_is_empty", dto.toBuilder().description("").build()),
                Arguments.of("description_is_longer_512", dto.toBuilder().description("a".repeat(513)).build()),
                Arguments.of("description_is_null", dto.toBuilder().description(null).build())
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
}