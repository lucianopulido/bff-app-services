package com.bff.app.services.domain.port.out;

import com.bff.app.services.domain.model.User;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ResendConfirmationCodeResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

public interface CognitoPort {
    Mono<SignUpResponse> createUser(User user);

    Mono<AuthenticationResultType> loginUser(User request);

    Mono<ForgotPasswordResponse> initiateForgotPassword(String email);

    Mono<ConfirmForgotPasswordResponse> confirmForgotPassword(String email, String confirmationCode, String newPassword);

    Mono<ResendConfirmationCodeResponse> resendVerificationCode(String email);

    Mono<ConfirmSignUpResponse> confirmEmail(String email, String code);
}
