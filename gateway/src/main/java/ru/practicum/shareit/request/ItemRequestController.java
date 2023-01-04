package ru.practicum.shareit.request;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoAdd;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;


    @PostMapping
    @Operation(summary = "add")
    public ResponseEntity<Object> add(@Valid @RequestBody ItemRequestDtoAdd dto,
                                      @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemRequestClient.add(dto, userId);
    }

    @GetMapping
    @Operation(summary = "findAllByUserId")
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemRequestClient.findAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "findById")
    public ResponseEntity<Object> findById(@PathVariable(value = "requestId") Long requestId,
                                           @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemRequestClient.findById(requestId, userId);
    }

    @GetMapping("/all")
    @Operation(summary = "findAllByPage")
    public ResponseEntity<Object> findAllByPage(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return itemRequestClient.findAllByPage(userId, from, size);
    }
}
