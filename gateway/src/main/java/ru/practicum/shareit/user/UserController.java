package ru.practicum.shareit.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.user.dto.UserDtoAdd;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userWebClient;

    @PostMapping
    @Operation(summary = "add")
    public Mono<ResponseEntity<String>> add(@Valid @RequestBody UserDtoAdd dto) {
        log.info("(add) Post User: {}", dto);
        return userWebClient.add((dto));
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "update")
    public Mono<ResponseEntity<String>> update(@Valid @RequestBody UserDtoUpdate dto,
                                               @PathVariable(value = "userId") Long userId) {
        log.info("(update) Patch User, userId: {},  dto: {}", userId, dto);
        return userWebClient.update(dto, userId);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "remove")
    public Mono<ResponseEntity<String>> remove(@PathVariable(value = "userId") Long userId) {
        log.info("(remove) Delete User, userId: {}", userId);
        return userWebClient.remove(userId);
    }

    @GetMapping
    @Operation(summary = "findAll")
    public Mono<ResponseEntity<String>> findAll() {
        log.info("(findAll) Get Users");
        return userWebClient.findAll();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "findById")
    public Mono<ResponseEntity<String>> findById(@PathVariable(value = "userId") Long userId) {
        log.info("(findById) Get User, userId: {}", userId);
        return userWebClient.findById(userId);
    }
}
