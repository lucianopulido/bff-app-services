package com.bff.app.services.infrastructure.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;

@RestControllerAdvice
public class GlobalExceptionHandler {


    private static final Map<String, Function<CognitoIdentityProviderException, ResponseEntity<ErrorDto>>> errorMap =
            Map.of(
                    "UsernameExistsException", ex -> createErrorResponse(
                            "User already exists",
                            ex.getMessage(),
                            HttpStatus.BAD_REQUEST
                    ),
                    "UserNotFoundException", ex -> createErrorResponse(
                            "User not found",
                            ex.getMessage(),
                            HttpStatus.NOT_FOUND
                    ),
                    "InvalidParameterException", ex -> createErrorResponse(
                            "Invalid parameters",
                            ex.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );

    @ExceptionHandler(CognitoIdentityProviderException.class)
    public Mono<ResponseEntity<ErrorDto>> handleCognitoException(CognitoIdentityProviderException ex) {
        return Mono.just(
                errorMap.getOrDefault(
                        ex.awsErrorDetails().errorCode(),
                        exception -> createErrorResponse(
                                "An unexpected error occurred",
                                exception.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR
                        )
                ).apply(ex)
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorDto>> handleGenericException(Exception ex) {
        return Mono.just(
                createErrorResponse(
                        "An unexpected error occurred",
                        ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
        );
    }

    private static ResponseEntity<ErrorDto> createErrorResponse(String error, String message, HttpStatus status) {
        ErrorDto errorDto = new ErrorDto(
                message,
                error,
                status.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(errorDto);
    }
}
