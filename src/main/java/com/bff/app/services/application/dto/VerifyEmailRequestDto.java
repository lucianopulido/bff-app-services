package com.bff.app.services.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VerifyEmailRequestDto {
    @NotEmpty
    @Email(message = "El email no tiene un formato válido.")
    private String email;
}
