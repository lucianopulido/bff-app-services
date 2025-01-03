package com.bff.app.services.infrastructure.adapters.in;

import com.bff.app.services.application.dto.ConfirmPasswordRequestDto;
import com.bff.app.services.application.dto.ForgotPasswordRequestDto;
import com.bff.app.services.application.dto.LoginRequestDto;
import com.bff.app.services.application.dto.LoginResponseDto;
import com.bff.app.services.application.dto.UserResponse;
import com.bff.app.services.application.dto.VerifyEmailDto;
import com.bff.app.services.application.dto.VerifyEmailRequestDto;
import com.bff.app.services.domain.port.in.UserUseCase;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> createUser(@Valid @RequestBody LoginRequestDto request) {
        return userUseCase.createOrder(request).map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponseDto>> loginUser(@Valid @RequestBody LoginRequestDto request) {
        return userUseCase.loginUser(request).map(ResponseEntity::ok);
    }

    @PostMapping("/forgot-password")
    public Mono<ResponseEntity<Void>> forgotPassword(@RequestBody ForgotPasswordRequestDto request) {
        return userUseCase.initiateForgotPassword(request.getEmail())
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @PostMapping("/confirm-password")
    public Mono<ResponseEntity<Void>> confirmPassword(@RequestBody ConfirmPasswordRequestDto request) {
        return userUseCase.confirmForgotPassword(
                request.getEmail(),
                request.getConfirmationCode(),
                request.getNewPassword()
        ).then(Mono.just(ResponseEntity.noContent().build()));
    }

    @PostMapping("/resend-verification-code")
    public Mono<ResponseEntity<Void>> resendVerificationCode(@RequestBody VerifyEmailRequestDto request) {
        return userUseCase.resendVerificationCode(request.getEmail())
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @PostMapping("/confirm-email")
    public Mono<ResponseEntity<Void>> confirmEmail(@RequestBody VerifyEmailDto request) {
        return userUseCase.confirmEmail(request.getEmail(), request.getCode())
                .then(Mono.just(ResponseEntity.noContent().build()));
    }


}
