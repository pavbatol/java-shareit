package ru.practicum.shareit.request;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

//@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestService requestService;


    @PostMapping
    @Operation(summary = "add")
//    public ItemRequestDto add(@Valid @RequestBody ItemRequestDto dto,
    public ItemRequestDto add(@RequestBody ItemRequestDto dto,
                              @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return requestService.add(dto, userId);
    }

    @GetMapping
    @Operation(summary = "findAllByUserId")
    public List<ItemRequestDto> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return requestService.findAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "findById")
    public ItemRequestDto findById(@PathVariable(value = "requestId") Long requestId,
                                   @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return requestService.findById(requestId, userId);
    }

    @GetMapping("/all")
    @Operation(summary = "findAllByPage")
    public List<ItemRequestDto> findAllByPage(@RequestHeader(X_SHARER_USER_ID) Long userId,
//                                              @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
//                                              @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
                                              @RequestParam(value = "from", defaultValue = "0") Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return requestService.findAllByPage(userId, from, size);
    }
}
