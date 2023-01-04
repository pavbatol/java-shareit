package ru.practicum.shareit.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
//@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
//    @Validated(OnAdd.class)
    @Operation(summary = "add")
//    public UserDto add(@Valid @RequestBody UserDto userDto) {
    public UserDto add(@RequestBody UserDto userDto) {
        return userService.add((userDto));
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "update")
//    public UserDto update(@Valid @RequestBody UserDto userDto,
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable(value = "userId") Long userId) {
        return userService.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "remove")
    public UserDto remove(@PathVariable(value = "userId") Long userId) {
        return userService.remove(userId);
    }

    @GetMapping
    @Operation(summary = "findAll")
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "findById")
    public UserDto findById(@PathVariable(value = "userId") Long userId) {
        return userService.findById(userId);
    }
}
