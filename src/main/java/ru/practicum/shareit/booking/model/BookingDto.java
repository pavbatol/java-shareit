package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.common.Entity;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookingDto implements Entity {

    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    private UserDto booker;

    @NotNull
    private ItemDto item;

    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;

    @NotNull
    private BookingStatus status;
}