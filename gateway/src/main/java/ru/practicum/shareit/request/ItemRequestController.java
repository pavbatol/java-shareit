package ru.practicum.shareit.request;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.request.dto.ItemRequestDtoAdd;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @Operation(summary = "add")
    public Mono<ResponseEntity<String>> add(@Valid @RequestBody ItemRequestDtoAdd dto,
                                            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("(add) Post Request with userId={}, dto={}", userId, dto);
        return itemRequestClient.add(dto, userId);
    }

    @GetMapping
    @Operation(summary = "findAllByUserId")
    public Mono<ResponseEntity<String>> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("(findAllByUserId) Get Requests with userId={}", userId);
        return itemRequestClient.findAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "findById")
    public Mono<ResponseEntity<String>> findById(@PathVariable(value = "requestId") Long requestId,
                                                 @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("(findById) Get Request with userId={}, requestId={}", userId, requestId);
        return itemRequestClient.findById(requestId, userId);
    }

    @GetMapping("/all")
    @Operation(summary = "findAllByPage")
    public Mono<ResponseEntity<String>> findAllByPage(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                      @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("(findAllByPage) Get Requests with userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.findAllByPage(userId, from, size);
    }
}
