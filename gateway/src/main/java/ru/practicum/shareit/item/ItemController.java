package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.item.dto.CommentDtoAdd;
import ru.practicum.shareit.item.dto.ItemDtoAdd;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    private final ItemClient itemWebClient;

    @PostMapping
    @Operation(summary = "add")
    public Mono<ResponseEntity<String>> add(@Valid @RequestBody ItemDtoAdd dto,
                                            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("(add) Patch Item with userId={}, dto={}", userId, dto);
        return itemWebClient.add(dto, userId);
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "update")
    public Mono<ResponseEntity<String>> update(@Valid @RequestBody ItemDtoUpdate dto,
                                               @PathVariable(value = "itemId") Long itemId,
                                               @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("(update) Patch Item with userId={}, itemId={}, dto={}", userId, itemId, dto);
        return itemWebClient.update(dto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "findById")
    public Mono<ResponseEntity<String>> findById(@PathVariable(value = "itemId") Long itemId,
                                                 @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("(findById) Get Item with userId={}", userId);
        return itemWebClient.findById(itemId, userId);
    }

    @GetMapping
    @Operation(summary = "findAllByUserId")
    public Mono<ResponseEntity<String>> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                        @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("(findAllByUserId) Get Items with userId={}, from={}, size={}", userId, from, size);
        return itemWebClient.findAllByUserId(userId, from, size);
    }

    @GetMapping("/search")
    @Operation(summary = "searchByNameOrDescription")
    public Mono<ResponseEntity<String>> searchByNameOrDescription(@RequestParam(value = "text") String text,
                                                                  @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                                  @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("(searchByNameOrDescription) Get Items with text={}, from={}, size={}", text, from, size);
        return itemWebClient.searchByNameOrDescription(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    @Operation(summary = "addComment")
    public Mono<ResponseEntity<String>> addComment(@Valid @RequestBody CommentDtoAdd dto,
                                                   @PathVariable(value = "itemId") Long itemId,
                                                   @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("(addComment) Post Comment with userId={}, itemId={}, dto={}", userId, itemId, dto);
        return itemWebClient.addComment(dto, itemId, userId);
    }
}
