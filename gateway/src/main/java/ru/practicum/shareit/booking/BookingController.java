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
    private final BookingClient bookingWebClient;

    @PostMapping
    @Operation(summary = "add")
    public Mono<ResponseEntity<String>> add(@Valid @RequestBody BookItemRequestDto dto,
                                            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Creating booking {}, userId={}", dto, userId);
        return bookingWebClient.add(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    @Operation(summary = "approve")
    public Mono<ResponseEntity<String>> approve(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                @PathVariable(value = "bookingId") Long bookingId,
                                                @RequestParam(value = "approved") Boolean approved) {
        return bookingWebClient.approve(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "findById")
    public Mono<ResponseEntity<String>> findById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                 @PathVariable(value = "bookingId") Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingWebClient.findById(bookingId, userId);
    }

    @GetMapping
    @Operation(summary = "findAllByBookerId")
    public Mono<ResponseEntity<String>> findAllByBookerId(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                                          @RequestParam(value = "state", defaultValue = ALL) String state,
                                                          @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                          @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new IllegalEnumException("Unknown state: " + state));
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, bookerId, from, size);
        return bookingWebClient.findAllByBookerId(bookerId, bookingState, from, size);
    }

    @GetMapping("/owner")
    @Operation(summary = "findAllByOwnerId")
    public Mono<ResponseEntity<String>> findAllByOwnerId(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                                         @RequestParam(value = "state", defaultValue = ALL) String state,
                                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new IllegalEnumException("Unknown state: " + state));
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, ownerId, from, size);
        return bookingWebClient.findAllByOwnerId(ownerId, bookingState, from, size);
    }
}
