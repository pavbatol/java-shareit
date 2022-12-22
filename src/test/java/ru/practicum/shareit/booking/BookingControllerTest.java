package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingDtoAdd;
import ru.practicum.shareit.booking.model.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookingControllerTest {

    public static final String URL_TEMPLATE = "/bookings";
    public static final String APPLICATION_JSON = "application/json";
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    public static final long ID_1 = 1L;
    public static final String ALL = "ALL";
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private BookingDtoAdd bookingDtoAdd1;
    private BookingDto bookingDto1;

    @BeforeEach
    void setUp() {
        bookingDtoAdd1 = makeBookingDtoAdd(ID_1);
        bookingDto1 = makeBookingDto(ID_1);
    }

    @SneakyThrows
    @Test
    void add_shouldInvokeServiceAndResponseCorrected_whenIncomingDtoIsValid() {
        long userId = ID_1;
        when(bookingService.add(bookingDtoAdd1, userId)).thenReturn(bookingDto1);

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoAdd1))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto1)))
                .andReturn().getResponse().getContentAsString();

        verify(bookingService, times(1)).add(bookingDtoAdd1, userId);
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithItemIdIsNull() {
        BookingDtoAdd incomingWithItemIdIsNull = bookingDtoAdd1.toBuilder().itemId(null).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingWithItemIdIsNull))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithStartIsNull() {
        BookingDtoAdd incomingWithStartIsNull = bookingDtoAdd1.toBuilder().start(null).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingWithStartIsNull))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithEndIsNull() {
        BookingDtoAdd incomingWithEndIsNull = bookingDtoAdd1.toBuilder().end(null).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingWithEndIsNull))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithStartIsPast() {
        LocalDateTime past = LocalDateTime.now().minusHours(1);
        BookingDtoAdd incomingWithStartIPast = bookingDtoAdd1.toBuilder().start(past).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingWithStartIPast))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithEndIsPast() {
        LocalDateTime past = LocalDateTime.now().minusHours(1);
        BookingDtoAdd incomingWithEndIPast = bookingDtoAdd1.toBuilder().end(past).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomingWithEndIPast))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    void approve_shouldInvokeServiceAndStatusIsOk() {
        long userId = ID_1;
        boolean approved = true;
        when(bookingService.approve(userId, userId, approved)).thenReturn(bookingDto1);

        mockMvc.perform(patch(URL_TEMPLATE + "/{bookingId}", userId)
                        .header(X_SHARER_USER_ID, userId)
                        .param("approved", String.valueOf(approved))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto1)));

        verify(bookingService, times(1)).approve(userId, userId, approved);
    }

    @SneakyThrows
    @Test
    void findById_shouldInvokeServiceAndStatusIsOk() {
        long userId = ID_1;
        long bookingId = ID_1;
        when(bookingService.findById(bookingId, userId)).thenReturn(bookingDto1);

        mockMvc.perform(get(URL_TEMPLATE + "/{bookingId}", bookingId)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto1)));

        verify(bookingService, times(1)).findById(bookingId, userId);
    }

    @SneakyThrows
    @Test
    void findAllByBookerId_shouldInvokeServiceAndResponseCorrect_whenParamsIsCorrect() {
        long userId = ID_1;
        int from = 1;
        int size = 1;
        when(bookingService.findAllByBookerId(userId, ALL, from, size)).thenReturn(List.of());

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                        .param("state", ALL)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(jsonPath("$.length()", is(0)));

        verify(bookingService, times(1)).findAllByBookerId(userId, ALL, from, size);
    }

    @SneakyThrows
    @Test
    void findAllByBookerId_shouldInvokeServiceAndResponseCorrect_whenParamsNotIs() {
        long userId = ID_1;
        int from = 0;
        int size = 10;
        when(bookingService.findAllByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto1, bookingDto1));

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(jsonPath("$.length()", is(2)));

        verify(bookingService, times(1)).findAllByBookerId(userId, ALL, from, size);
    }

    @SneakyThrows
    @Test
    void findAllByBookerId_shouldBadRequest_whenFromIsNegative() {
        long userId = ID_1;
        int from = -1;
        int size = 1;

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                        .param("state", ALL)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    void findAllByBookerId_shouldBadRequest_whenSizeIsZero() {
        long userId = ID_1;
        int from = 1;
        int size = 0;

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                        .param("state", ALL)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingService);
    }


    @SneakyThrows
    @Test
    void findAllByOwnerId_shouldInvokeServiceAndResponseCorrect_whenParamsIsCorrect() {
        long userId = ID_1;
        int from = 1;
        int size = 1;
        when(bookingService.findAllByBookerId(userId, ALL, from, size)).thenReturn(List.of());

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                        .param("state", ALL)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(jsonPath("$.length()", is(0)));

        verify(bookingService, times(1)).findAllByBookerId(userId, ALL, from, size);
    }

    @SneakyThrows
    @Test
    void findAllByOwnerId_shouldInvokeServiceAndResponseCorrect_whenParamsNotIs() {
        long userId = ID_1;
        int from = 0;
        int size = 10;
        when(bookingService.findAllByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto1, bookingDto1));

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(jsonPath("$.length()", is(2)));

        verify(bookingService, times(1)).findAllByBookerId(userId, ALL, from, size);
    }

    @SneakyThrows
    @Test
    void findAllByOwnerId_shouldBadRequest_whenFromIsNegative() {
        long userId = ID_1;
        int from = -1;
        int size = 1;

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                        .param("state", ALL)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    void findAllByOwnerId_shouldBadRequest_whenSizeIsZero() {
        long userId = ID_1;
        int from = 1;
        int size = 0;

        mockMvc.perform(get(URL_TEMPLATE)
                        .header(X_SHARER_USER_ID, userId)
                        .param("state", ALL)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingService);
    }


    private BookingDtoAdd makeBookingDtoAdd(long id) {
        return new BookingDtoAdd(
                id,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                BookingStatus.WAITING
        );
    }

    private BookingDto makeBookingDto(long id) {
        return new BookingDto(
                id,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                BookingStatus.WAITING
        );
    }
}
