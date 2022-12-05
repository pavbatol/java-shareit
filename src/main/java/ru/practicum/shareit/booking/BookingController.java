package ru.practicum.shareit.booking;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.common.OnAdd;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @Validated(OnAdd.class)
    @Operation(summary = "add")
    public BookingDto add(@Valid @RequestBody BookingDto bookingDto,
                          @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return null;
    }

    @PatchMapping("/{bookingId}")
    @Operation(summary = "approve")
    public BookingDto approve(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @PathVariable(value = "bookingId") Long bookingId,
                                     @RequestParam(value = "approved", required = true) Boolean approved) {
        return null;
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "findById")
    public BookingDto findById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                               @PathVariable(value = "bookingId") Long bookingId) {
        return null;
    }

    @GetMapping
    @Operation(summary = "findAllByBookerId")
    public List<BookingDto> findAllByBookerId(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                              @RequestParam(value = "state", defaultValue = "ALL") String state) {
        return null;
    }

    @GetMapping("/bookings/owner")
    @Operation(summary = "findAllByOwnerId")
    public List<BookingDto> findAllByOwnerId(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                             @RequestParam(value = "state", defaultValue = "ALL") String state) {
        return null;
    }
}
