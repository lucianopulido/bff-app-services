package com.bff.app.services.domain.port.in;

import com.bff.app.services.application.dto.CreateUserRequest;
import com.bff.app.services.application.dto.UserResponse;
import reactor.core.publisher.Mono;

public interface UserUseCase {
    Mono<UserResponse> createOrder(CreateUserRequest request);
}
