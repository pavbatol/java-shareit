package ru.practicum.shareit.booking.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.enums.BookingStatus;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> bookingDtoJacksonTester;

    @SneakyThrows
    @Test
    void testJson() {
        UserDto userDto = new UserDto(
                1L,
                "userName",
                "email@Email.ru"
        );

        ItemDto itemDto = new ItemDto(
                1L,
                "itemName",
                "ItemDescription",
                true,
                null
        );


        BookingDto bookingDto = new BookingDto(
                1L,
                userDto,
                itemDto,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                BookingStatus.WAITING
        );

        JsonContent<BookingDto> jsonContent = bookingDtoJacksonTester.write(bookingDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.name").isEqualTo("userName");
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.name").isEqualTo("itemName");
        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingDto.getStart().format(DateTimeFormatter.ISO_DATE_TIME));
        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingDto.getEnd().format(DateTimeFormatter.ISO_DATE_TIME));
    }
}