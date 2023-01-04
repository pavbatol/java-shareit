package ru.practicum.shareit.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoAdd;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @PostMapping
    @Operation(summary = "add")
    public ResponseEntity<Object> add(@Valid @RequestBody UserDtoAdd dto) {
        return userClient.add((dto));
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "update")
    public ResponseEntity<Object> update(@Valid @RequestBody UserDtoUpdate dto,
                                         @PathVariable(value = "userId") Long userId) {
        return userClient.update(dto, userId);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "remove")
    public ResponseEntity<Object> remove(@PathVariable(value = "userId") Long userId) {
        return userClient.remove(userId);
    }

    @GetMapping
    @Operation(summary = "findAll")
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "findById")
    public ResponseEntity<Object> findById(@PathVariable(value = "userId") Long userId) {
        return userClient.findById(userId);
    }
}
