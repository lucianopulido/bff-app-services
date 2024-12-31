package com.bff.app.services.domain.port.out;

import com.bff.app.services.domain.model.User;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

public interface CognitoPort {
    Mono<SignUpResponse> createUser(User user);

    Mono<AuthenticationResultType> loginUser(User request);
}
