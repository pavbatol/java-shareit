package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
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

    UserDto userDto1;

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
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
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
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
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
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
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
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
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
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
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
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
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
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
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
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
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
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
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
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
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

    private UserDto makeUserDto(long id) {
        return new UserDto(
                id,
                "name_" + id,
                "email_" + id + "@emal.ru"
        );
    }
}