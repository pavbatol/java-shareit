package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException;
import ru.practicum.shareit.exeption.AlreadyExistsException;
import ru.practicum.shareit.exeption.IllegalEnumException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    public static final String URL_TEMPLATE = "/users";
    public static final String APPLICATION_JSON = "application/json";
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    public static final long ID_1 = 1L;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDto userDto1;

    @BeforeEach
    void setUp() {
        userDto1 = makeUserDto(ID_1);
    }

    @SneakyThrows
    @Test
    void add_shouldInvokeServiceAndResponseCorrected_whenIncomingDtoIsValid() {
        when(userService.add(userDto1)).thenReturn(userDto1);

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto1))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto1)))
                .andReturn().getResponse().getContentAsString();

        verify(userService, times(1)).add(userDto1);
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithNameIsNull() {
        UserDto badUserDto = userDto1.toBuilder().name(null).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badUserDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithNameIsBlank() {
        UserDto badUserDto = userDto1.toBuilder().name(" ").build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badUserDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithNameLengthIsGrater50() {
        UserDto badUserDto = userDto1.toBuilder().name(String.valueOf(new char[51])).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badUserDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithEmailIsNull() {
        UserDto badUserDto = userDto1.toBuilder().email(null).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badUserDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithEmailIsBlank() {
        UserDto badUserDto = userDto1.toBuilder().email(" ").build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badUserDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void add_shouldBadRequest_whenIncomingDtoWithBadEmail() {
        UserDto badUserDto = userDto1.toBuilder().email("email.ru").build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badUserDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void update_shouldInvokeServiceAndResponseCorrected_whenIncomingDtoIsValid() {
        long userId = ID_1;
        when(userService.update(userDto1, userId)).thenReturn(userDto1);

        mockMvc.perform(patch(URL_TEMPLATE + "/{userId}", userId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto1))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto1)));

        verify(userService, times(1)).update(userDto1, userId);
    }

    @SneakyThrows
    @Test
    void update_shouldBadRequest_whenIncomingDtoWithNameLengthIsGrater50() {
        UserDto badUserDto = userDto1.toBuilder().name(String.valueOf(new char[51])).build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badUserDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void update_shouldBadRequest_whenIncomingDtoWithNameIsBlank() {
        UserDto badUserDto = userDto1.toBuilder().name(" ").build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badUserDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void update_shouldBadRequest_whenIncomingDtoWithEmailIsBlank() {
        UserDto badUserDto = userDto1.toBuilder().email(" ").build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badUserDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void update_shouldBadRequest_whenIncomingDtoWithBadEmail() {
        UserDto badUserDto = userDto1.toBuilder().email("email.ru").build();

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badUserDto))
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void remove() {
        long userId = ID_1;
        when(userService.remove(userId)).thenReturn(userDto1);

        mockMvc.perform(delete(URL_TEMPLATE + "/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto1)));

        verify(userService, times(1)).remove(userId);
    }

    @SneakyThrows
    @Test
    void findAll_shouldInvokeServiceAndStatusIsOk() {
        when(userService.findAll()).thenReturn(List.of(userDto1));

        mockMvc.perform(get(URL_TEMPLATE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));

        verify(userService, times(1)).findAll();
    }

    @SneakyThrows
    @Test
    void findById_shouldInvokeServiceAndStatusIsOk() {
        long userId = ID_1;
        when(userService.findById(userId)).thenReturn(userDto1);

        mockMvc.perform(get(URL_TEMPLATE + "/{userId}", userId)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto1)));

        verify(userService, times(1)).findById(userId);
    }


    @SneakyThrows
    @Test
    void restExceptionHandler_shouldCatchNotFound_whenThrowsNotFoundException() {
        long userId = ID_1;
        NotFoundException exception = new NotFoundException("exception");
        when(userService.findById(userId)).thenThrow(exception);

        mockMvc.perform(get(URL_TEMPLATE + "/{userId}", userId)
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
    }

    @SneakyThrows
    @Test
    void restExceptionHandler_shouldCatchConflict_whenThrowsAlreadyExistsException() {
        long userId = ID_1;
        AlreadyExistsException exception = new AlreadyExistsException("exception");
        when(userService.findById(userId)).thenThrow(exception);

        mockMvc.perform(get(URL_TEMPLATE + "/{userId}", userId)
                )
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AlreadyExistsException));
    }

    @SneakyThrows
    @Test
    void restExceptionHandler_shouldCatchBadRequest_whenThrowsIllegalArgumentException() {
        long userId = ID_1;
        IllegalArgumentException exception = new IllegalArgumentException("exception");
        when(userService.findById(userId)).thenThrow(exception);

        mockMvc.perform(get(URL_TEMPLATE + "/{userId}", userId)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException));
    }

    @SneakyThrows
    @Test
    void restExceptionHandler_shouldCatchBadRequest_whenThrowsIllegalEnumException() {
        long userId = ID_1;
        IllegalEnumException exception = new IllegalEnumException("exception");
        when(userService.findById(userId)).thenThrow(exception);

        mockMvc.perform(get(URL_TEMPLATE + "/{userId}", userId)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalEnumException));
    }

    @SneakyThrows
    @Test
    void restExceptionHandler_shouldCatchBadRequest_whenThrowsHttpMessageNotReadableException() {
        long userId = ID_1;
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("exception");
        when(userService.findById(userId)).thenThrow(exception);

        mockMvc.perform(get(URL_TEMPLATE + "/{userId}", userId)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException));
    }

    @SneakyThrows
    @Test
    void restExceptionHandler_shouldCatchBadRequest_whenThrowsNoHandlerFoundException() {
        long userId = ID_1;

        mockMvc.perform(get(URL_TEMPLATE + "/wrong_path/{userId}", userId)
                )
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void restExceptionHandler_shouldCatchBadRequest_whenThrowsHttpServerErrorException() {
        long userId = ID_1;
        HttpServerErrorException exception = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        when(userService.findById(userId)).thenThrow(exception);

        mockMvc.perform(get(URL_TEMPLATE + "/{userId}", userId)
                        .header(X_SHARER_USER_ID, ID_1)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpServerErrorException));
    }

    private UserDto makeUserDto(long id) {
        return new UserDto(
                id,
                "name_" + id,
                "email_" + id + "@emal.ru"
        );
    }
}