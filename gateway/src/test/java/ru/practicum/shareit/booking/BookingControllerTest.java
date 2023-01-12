package ru.practicum.shareit.booking;

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
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

    private static final String URL_TEMPLATE = "/bookings";
    private static final String APPLICATION_JSON = "application/json";
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private static final long ID_1 = 1L;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private BookingClient client;

    @SneakyThrows
    @Test
    void add_shouldStatusOk_whenInputDtoIsValid() {
        final long userId = ID_1;
        final BookItemRequestDto dto = makeBookItemRequestDto(ID_1);

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk());

        verify(client, times(1)).add(dto, userId);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeBadDtoAdd")
    void add_shouldStatusBad_whenInputDtoIsNotValid(String name, BookItemRequestDto dto) {
        final long userId = ID_1;

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @SneakyThrows
    @Test
    void approve_shouldInvokeClient() {
        final long userId = ID_1;
        final long bookingId = ID_1;
        Boolean approved = true;

        mockMvc.perform((patch(URL_TEMPLATE + "/{bookingId}?approved={approved}", bookingId, approved))
                        .contentType(APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk());

        verify(client, times(1)).approve(bookingId, userId, approved);
    }

    @SneakyThrows
    @Test
    void findById_shouldInvokeClient() {
        final long userId = ID_1;
        final long bookingId = ID_1;
        mockMvc.perform(get(URL_TEMPLATE + "/{bookingId}", bookingId)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk());

        verify(client, times(1)).findById(bookingId, userId);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}}")
    @MethodSource("makeValidFromAndSizeAndState")
    void findAllByBookerId_shouldInvokeClient_whenValidParameters(String name, Integer from, Integer size, String state) {
        final long userId = ID_1;

        mockMvc.perform(get(URL_TEMPLATE + "?from={from}&size={size}&state={state}", from, size, state)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk());

        verify(client, times(1)).findAllByBookerId(
                userId,
                state != null ? BookingState.valueOf(state) : BookingState.ALL,
                from != null ? from : 0,
                size != null ? size : 10);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}}")
    @MethodSource("makeBadFromAndSizeAndState")
    void findAllByBookerId_shouldInvokeClient_whenBadParameters(String name, Integer from, Integer size, String state) {
        final long userId = ID_1;

        mockMvc.perform(get(URL_TEMPLATE + "?from={from}&size={size}&state={state}", from, size, state)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}}")
    @MethodSource("makeValidFromAndSizeAndState")
    void findAllByOwnerId_shouldInvokeClient_whenValidParameters(String name, Integer from, Integer size, String state) {
        final long userId = ID_1;

        mockMvc.perform(get(URL_TEMPLATE + "/owner?from={from}&size={size}&state={state}", from, size, state)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk());

        verify(client, times(1)).findAllByOwnerId(
                userId,
                state != null ? BookingState.valueOf(state) : BookingState.ALL,
                from != null ? from : 0,
                size != null ? size : 10);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}}")
    @MethodSource("makeBadFromAndSizeAndState")
    void findAllByOwnerId_shouldInvokeClient_whenBadParameters(String name, Integer from, Integer size, String state) {
        final long userId = ID_1;

        mockMvc.perform(get(URL_TEMPLATE + "/owner?from={from}&size={size}&state={state}", from, size, state)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    private static BookItemRequestDto makeBookItemRequestDto(long id) {
        return new BookItemRequestDto(
                id,
                1,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
    }

    public static Stream<Arguments> makeBadDtoAdd() {
        BookItemRequestDto dto = makeBookItemRequestDto(ID_1);
        return Stream.of(
                Arguments.of("start_is_past", dto.toBuilder().start(LocalDateTime.now().minusSeconds(1)).build()),
                Arguments.of("start_is_null", dto.toBuilder().start(null).build()),
                Arguments.of("end_is_now", dto.toBuilder().end(LocalDateTime.now()).build()),
                Arguments.of("end_is_null", dto.toBuilder().end(null).build())
        );
    }

    public static Stream<Arguments> makeValidFromAndSizeAndState() {
        return Stream.of(
                Arguments.of("all_is_valid", 0, 10, BookingState.WAITING.name()),
                Arguments.of("all_is_valid", null, null, null)
        );
    }

    public static Stream<Arguments> makeBadFromAndSizeAndState() {
        return Stream.of(
                Arguments.of("from_is_negative", -1, 10, BookingState.ALL.name()),
                Arguments.of("size_is_zero", 1, 0, BookingState.ALL.name()),
                Arguments.of("state_is_unknown", 0, 10, "UNKNOWN_STATE")
        );
    }
}