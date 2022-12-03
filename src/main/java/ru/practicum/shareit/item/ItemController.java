package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.OnAdd;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    @Validated(OnAdd.class)
    @Operation(summary = "add")
    public ItemDto add(@Valid @RequestBody ItemDto itemDto,
                       @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.add(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "update")
    public ItemDto update(@Valid @RequestBody ItemDto itemDto,
                          @PathVariable(value = "itemId") Long itemId,
                          @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "findById")
    public ItemDto findById(@PathVariable(value = "itemId") Long itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping
    @Operation(summary = "findAllByUserId")
    public List<ItemDto> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.findAllByUserId(userId);
    }

    @GetMapping("/search")
    @Operation(summary = "searchByNameOrDescription")
    public List<ItemDto> searchByNameOrDescription(@RequestParam(value = "text") String text) {
        return itemService.searchByNameOrDescription(text);
    }
}
