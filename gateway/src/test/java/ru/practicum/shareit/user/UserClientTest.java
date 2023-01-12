package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoAdd;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserClientTest {
    private static final String URL_TEMPLATE = "/users";
    private static final String APPLICATION_JSON = "application/json";
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private static final long ID_1 = 1L;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private UserClient client;

    private final UserDtoAdd userDtoAdd1 = makeUserDtoAdd(ID_1);

    @SneakyThrows
    @Test
    void add_shouldStatusOk_whenInputDtoIsValid() {
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoAdd1))
                )
                .andExpect(status().isOk());

        verify(client, times(1)).add(userDtoAdd1);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeBadDtoAdd")
    void add_shouldStatusBad_whenInputDtoIsNotValid(String name, UserDtoAdd dtoAdd) {
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAdd))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeValidDtoUpdate")
    void update_shouldStatusOk_whenInputDtoIsValid(String name, UserDtoUpdate userDtoUpdate) {
        final long userId = ID_1;

        mockMvc.perform(patch(URL_TEMPLATE + "/{userId}", userId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoUpdate))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk());

        verify(client, times(1)).update(userDtoUpdate, userId);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeBadDtoUpdate")
    void update_shouldStatusBad_whenInputDtoIsNotValid(String name, UserDtoUpdate userDtoUpdate) {
        final long userId = ID_1;

        mockMvc.perform(patch(URL_TEMPLATE + "/{userId}", userId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoUpdate))
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @SneakyThrows
    @Test
    void remove_shouldInvokeClient() {
        final long userId = ID_1;

        mockMvc.perform(delete(URL_TEMPLATE + "/{userId}", userId))
                .andExpect(status().isOk());

        verify(client, times(1)).remove(userId);
    }

    @SneakyThrows
    @Test
    void findAll_shouldInvokeClient() {
        mockMvc.perform(get(URL_TEMPLATE)
                )
                .andExpect(status().isOk());

        verify(client, times(1)).findAll();
    }

    @SneakyThrows
    @Test
    void findById_shouldInvokeClient() {
        final long userId = ID_1;

        mockMvc.perform(get(URL_TEMPLATE + "/{userId}", userId)
                        .header(X_SHARER_USER_ID, userId)
                )
                .andExpect(status().isOk());

        verify(client, times(1)).findById(userId);
    }

    private UserDto makeUserDto(long id) {
        return new UserDto(
                id,
                "name_" + id,
                "email_" + id + "@emal.ru"
        );
    }

    private static UserDtoAdd makeUserDtoAdd(long id) {
        return new UserDtoAdd(
                id,
                "nameADD_" + id,
                "emailADD_" + id + "@emal.ru"
        );
    }

    private static UserDtoUpdate makeUserDtoUpdate(long id) {
        return new UserDtoUpdate(
                id,
                "nameUpdate_" + id,
                "emailUpdate_" + id + "@emal.ru"
        );

    }

    private static Stream<Arguments> makeBadDtoAdd() {
        UserDtoAdd userDtoAdd = makeUserDtoAdd(ID_1);
        return Stream.of(
                Arguments.of("name_is_empty", userDtoAdd.toBuilder().name("").build()),
                Arguments.of("name_is_blank", userDtoAdd.toBuilder().name(" ").build()),
                Arguments.of("name_is_longer_50", userDtoAdd.toBuilder().name("a".repeat(51)).build()),
                Arguments.of("name_is_null", userDtoAdd.toBuilder().name(null).build()),

                Arguments.of("email_is_empty", userDtoAdd.toBuilder().email("").build()),
                Arguments.of("email_is_blank", userDtoAdd.toBuilder().email(" ").build()),
                Arguments.of("email_is_bad", userDtoAdd.toBuilder().email("email.ru").build()),
                Arguments.of("email_is_null", userDtoAdd.toBuilder().email(null).build())
        );
    }

    private static Stream<Arguments> makeBadDtoUpdate() {
        UserDtoUpdate userDtoUpdate = makeUserDtoUpdate(ID_1);
        return Stream.of(
                Arguments.of("name_is_empty", userDtoUpdate.toBuilder().name("").build()),
                Arguments.of("name_is_blank", userDtoUpdate.toBuilder().name(" ").build()),
                Arguments.of("name_is_longer_50", userDtoUpdate.toBuilder().name("a".repeat(51)).build()),

                Arguments.of("email_is_empty", userDtoUpdate.toBuilder().email("").build()),
                Arguments.of("email_is_blank", userDtoUpdate.toBuilder().email(" ").build()),
                Arguments.of("email_is_bad", userDtoUpdate.toBuilder().email("email.ru").build())
        );
    }

    private static Stream<Arguments> makeValidDtoUpdate() {
        UserDtoUpdate userDtoUpdate = makeUserDtoUpdate(ID_1);
        return Stream.of(
                Arguments.of("all_is_valid", userDtoUpdate),
                Arguments.of("name_is_null", userDtoUpdate.toBuilder().name(null).build()),
                Arguments.of("email_is_null", userDtoUpdate.toBuilder().email(null).build())
        );
    }
}