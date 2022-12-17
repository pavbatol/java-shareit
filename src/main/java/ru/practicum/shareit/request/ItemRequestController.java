package ru.practicum.shareit.request;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestService requestService;


    @PostMapping
    @Operation(summary = "add")
    public ItemRequestDto add(@Valid @RequestBody ItemRequestDto dto,
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

}
