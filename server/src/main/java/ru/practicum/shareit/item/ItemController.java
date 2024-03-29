package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.model.CommentDto;
import ru.practicum.shareit.item.comment.model.CommentDtoShort;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "add")
    public ItemDto add(@RequestBody ItemDto itemDto,
                       @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.add(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "update")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable(value = "itemId") Long itemId,
                          @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "findById")
    public ItemDtoResponse findById(@PathVariable(value = "itemId") Long itemId,
                                    @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.findById(itemId, userId);
    }

    @GetMapping
    @Operation(summary = "findAllByUserId")
    public List<ItemDtoResponse> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                 @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return itemService.findAllByUserId(userId, from, size);
    }

    @GetMapping("/search")
    @Operation(summary = "searchByNameOrDescription")
    public List<ItemDto> searchByNameOrDescription(@RequestParam(value = "text") String text,
                                                   @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return itemService.searchByNameOrDescription(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    @Operation(summary = "addComment")
    public CommentDtoShort addComment(@RequestBody CommentDto commentDto,
                                      @PathVariable(value = "itemId") Long itemId,
                                      @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return commentService.add(commentDto, itemId, userId);
    }
}
