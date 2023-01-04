package ru.practicum.shareit.booking;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private static final String ALL = "ALL";
    private final BookingClient bookingClient;

    @PostMapping
    @Operation(summary = "add")
    public ResponseEntity<Object> add(@Valid @RequestBody BookItemRequestDto dto,
                                      @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Creating booking {}, userId={}", dto, userId);
        return bookingClient.add(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    @Operation(summary = "approve")
    public ResponseEntity<Object> approve(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                          @PathVariable(value = "bookingId") Long bookingId,
                                          @RequestParam(value = "approved") Boolean approved) {
        return bookingClient.approve(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "findById")
    public ResponseEntity<Object> findById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                           @PathVariable(value = "bookingId") Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.findById(bookingId, userId);
    }

    @GetMapping
    @Operation(summary = "findAllByBookerId")
    public ResponseEntity<Object> findAllByBookerId(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                                    @RequestParam(value = "state", defaultValue = ALL) String state,
                                                    @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, bookerId, from, size);
        return bookingClient.findAllByBookerId(bookerId, bookingState, from, size);
    }

    @GetMapping("/owner")
    @Operation(summary = "findAllByOwnerId")
    public ResponseEntity<Object> findAllByOwnerId(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                                   @RequestParam(value = "state", defaultValue = ALL) String state,
                                                   @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, ownerId, from, size);
        return bookingClient.findAllByOwnerId(ownerId, bookingState, from, size);
    }
}
