package com.bff.app.services.domain.port.in;

import com.bff.app.services.application.dto.LoginRequestDto;
import com.bff.app.services.application.dto.LoginResponseDto;
import com.bff.app.services.application.dto.UserResponse;
import reactor.core.publisher.Mono;

public interface UserUseCase {
    Mono<UserResponse> createOrder(LoginRequestDto request);

    Mono<LoginResponseDto> loginUser(LoginRequestDto request);

    Mono<Void> initiateForgotPassword(String email);

    Mono<Void> confirmForgotPassword(String email, String confirmationCode, String newPassword);
}
