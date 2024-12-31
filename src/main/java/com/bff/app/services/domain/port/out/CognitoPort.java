package com.bff.app.services.domain.port.out;

import com.bff.app.services.application.dto.LoginResponseDto;
import com.bff.app.services.application.dto.UserResponse;
import com.bff.app.services.domain.model.User;
import reactor.core.publisher.Mono;

public interface CognitoPort {
    Mono<UserResponse> createUser(User user);

    Mono<LoginResponseDto> loginUser(User request);
}
