package com.bff.app.services.infrastructure.adapters.in;

import com.bff.app.services.application.dto.CreateUserRequest;
import com.bff.app.services.application.dto.UserResponse;
import com.bff.app.services.domain.port.in.UserUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserUseCase orderUseCase;

    public UserController(UserUseCase userUseCase) {
        this.orderUseCase = userUseCase;
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> createUser(@RequestBody CreateUserRequest request) {
        return orderUseCase.createOrder(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error creating user: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

}
