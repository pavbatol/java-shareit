package ru.practicum.shareit.booking;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingDtoAdd;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private static final String ALL = "ALL";

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "add")
    public BookingDto add(@RequestBody BookingDtoAdd dto,
                          @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return bookingService.add(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    @Operation(summary = "approve")
    public BookingDto approve(@RequestHeader(X_SHARER_USER_ID) Long userId,
                              @PathVariable(value = "bookingId") Long bookingId,
                              @RequestParam(value = "approved") Boolean approved) {
        return bookingService.approve(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "findById")
    public BookingDto findById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                               @PathVariable(value = "bookingId") Long bookingId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    @Operation(summary = "findAllByBookerId")
    public List<BookingDto> findAllByBookerId(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                              @RequestParam(value = "state", defaultValue = ALL) String state,
                                              @RequestParam(value = "from", defaultValue = "0") Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return bookingService.findAllByBookerId(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    @Operation(summary = "findAllByOwnerId")
    public List<BookingDto> findAllByOwnerId(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                             @RequestParam(value = "state", defaultValue = ALL) String state,
                                             @RequestParam(value = "from", defaultValue = "0") Integer from,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return bookingService.findAllByOwnerId(ownerId, state, from, size);
    }
}
