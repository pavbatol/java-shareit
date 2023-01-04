package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoAdd;
import ru.practicum.shareit.item.dto.ItemDtoAdd;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @PostMapping
    @Operation(summary = "add")
    public ResponseEntity<Object> add(@Valid @RequestBody ItemDtoAdd dtp,
                                      @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemClient.add(dtp, userId);
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "update")
    public ResponseEntity<Object> update(@Valid @RequestBody ItemDtoUpdate dto,
                                         @PathVariable(value = "itemId") Long itemId,
                                         @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemClient.update(dto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "findById")
    public ResponseEntity<Object> findById(@PathVariable(value = "itemId") Long itemId,
                                           @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemClient.findById(itemId, userId);
    }

    @GetMapping
    @Operation(summary = "findAllByUserId")
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                  @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return itemClient.findAllByUserId(userId, from, size);
    }

    @GetMapping("/search")
    @Operation(summary = "searchByNameOrDescription")
    public ResponseEntity<Object> searchByNameOrDescription(@RequestParam(value = "text") String text,
                                                            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return itemClient.searchByNameOrDescription(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    @Operation(summary = "addComment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDtoAdd dto,
                                             @PathVariable(value = "itemId") Long itemId,
                                             @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemClient.addComment(dto, itemId, userId);
    }
}
