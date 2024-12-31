package com.bff.app.services.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    @NotEmpty
    @Email(message = "El email no tiene un formato válido.")
    private String email;
    @NotEmpty
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe incluir al menos una letra mayúscula, una minúscula, un número y un " +
                    "carácter especial."
    )
    private String password;
}
