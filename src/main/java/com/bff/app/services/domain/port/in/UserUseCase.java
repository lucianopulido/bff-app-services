package com.bff.app.services.domain.port.in;

import com.bff.app.services.application.dto.LoginRequestDto;
import com.bff.app.services.application.dto.LoginResponseDto;
import com.bff.app.services.application.dto.UserResponse;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ResendConfirmationCodeResponse;

public interface UserUseCase {
    Mono<UserResponse> createOrder(LoginRequestDto request);

    Mono<LoginResponseDto> loginUser(LoginRequestDto request);

    Mono<ForgotPasswordResponse> initiateForgotPassword(String email);

    Mono<ConfirmForgotPasswordResponse> confirmForgotPassword(String email, String confirmationCode, String newPassword);

    Mono<ResendConfirmationCodeResponse> resendVerificationCode(String email);

    Mono<ConfirmSignUpResponse> confirmEmail(String email, String code);
}
