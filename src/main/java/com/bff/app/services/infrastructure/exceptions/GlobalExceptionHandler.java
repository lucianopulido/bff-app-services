package com.bff.app.services.infrastructure.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

@Slf4j
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
                    ),
                    "NotAuthorizedException", ex -> createErrorResponse(
                            "Invalid credentials",
                            ex.getMessage(),
                            HttpStatus.UNAUTHORIZED
                    ),
                    "UserNotConfirmedException", ex -> createErrorResponse(
                            "User is not confirmed",
                            ex.getMessage(),
                            HttpStatus.UNAUTHORIZED
                    ),
                    "TooManyRequestsException", ex -> createErrorResponse(
                            "Too many requests",
                            ex.getMessage(),
                            HttpStatus.TOO_MANY_REQUESTS
                    )
            );

    @ExceptionHandler(CognitoIdentityProviderException.class)
    public Mono<ResponseEntity<ErrorDto>> handleCognitoException(CognitoIdentityProviderException ex) {
        String errorCode = ex.awsErrorDetails().errorCode();

        if (!errorMap.containsKey(errorCode)) {
            log.error("Unmapped Cognito error: {} ", errorCode);
        }

        return Mono.just(
                errorMap.getOrDefault(
                        errorCode,
                        exception -> createErrorResponse(
                                "An unexpected error occurred",
                                exception.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR
                        )
                ).apply(ex)
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ErrorDto>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ArrayList<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(error.getDefaultMessage())
        );
        String message = "Validation failed for one or more fields.";
        return Mono.just(
                createErrorResponse(
                        message,
                        errors.toString(),
                        HttpStatus.BAD_REQUEST
                )
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    protected Mono<ResponseEntity<ErrorDto>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Malformed JSON request", ex);
        return Mono.just(
                createErrorResponse(
                        "Malformed JSON request",
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST
                )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    protected Mono<ResponseEntity<ErrorDto>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("Argument type mismatch", ex);
        String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        return Mono.just(
                createErrorResponse(
                        "Argument type mismatch",
                        message,
                        HttpStatus.BAD_REQUEST
                )
        );
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseBody
    protected Mono<ResponseEntity<ErrorDto>> handleWebExchangeBindException(WebExchangeBindException ex) {
        ArrayList<String> errors = new ArrayList<>();
        ex.getFieldErrors().forEach(error ->
                errors.add(error.getDefaultMessage())
        );
        String message = "Validation failed for one or more fields.";
        return Mono.just(
                createErrorResponse(
                        message,
                        errors.toString(),
                        HttpStatus.BAD_REQUEST
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorDto>> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
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
