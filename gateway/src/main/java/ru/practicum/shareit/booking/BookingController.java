package ru.practicum.shareit.booking;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exeption.IllegalEnumException;

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
    public Mono<ResponseEntity<String>> add(@Valid @RequestBody BookItemRequestDto dto,
                                            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("(add) Post Booking with booking={}, userId={}", dto, userId);
        return bookingClient.add(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    @Operation(summary = "approve")
    public Mono<ResponseEntity<String>> approve(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                @PathVariable(value = "bookingId") Long bookingId,
                                                @RequestParam(value = "approved") Boolean approved) {
        log.info("(approve) Patch Booking with userId={}, bookingId={}, approved={}", userId, bookingId, approved);
        return bookingClient.approve(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "findById")
    public Mono<ResponseEntity<String>> findById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                 @PathVariable(value = "bookingId") Long bookingId) {
        log.info("(findById) Get Booking with booking={}, userId={}", bookingId, userId);
        return bookingClient.findById(bookingId, userId);
    }

    @GetMapping
    @Operation(summary = "findAllByBookerId")
    public Mono<ResponseEntity<String>> findAllByBookerId(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                                          @RequestParam(value = "state", defaultValue = ALL) String state,
                                                          @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                          @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        BookingState bookingState = getBookingState(state);
        log.info("(findAllByBookerId) Get Booking with state={}, userId={}, from={}, size={}", state, bookerId, from, size);
        return bookingClient.findAllByBookerId(bookerId, bookingState, from, size);
    }

    @GetMapping("/owner")
    @Operation(summary = "findAllByOwnerId")
    public Mono<ResponseEntity<String>> findAllByOwnerId(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                                         @RequestParam(value = "state", defaultValue = ALL) String state,
                                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        BookingState bookingState = getBookingState(state);
        log.info("(findAllByOwnerId) Get Booking with state={}, userId={}, from={}, size={}", state, ownerId, from, size);
        return bookingClient.findAllByOwnerId(ownerId, bookingState, from, size);
    }

    private BookingState getBookingState(String state) {
        return BookingState.from(state)
                .orElseThrow(() -> new IllegalEnumException("Unknown state: " + state));
    }
}
