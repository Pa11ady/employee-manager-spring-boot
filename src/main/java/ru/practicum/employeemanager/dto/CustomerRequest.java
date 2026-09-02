package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Данные для создания нового клиента")
public record CustomerRequest(
        @Schema(description = "Имя клиента", example = "Иван")
        @NotBlank
        @Size(min = 1, max = 100)
        String name,

        @Schema(description = "Фамилия клиента", example = "Иванов")
        @NotBlank
        @Size(min = 1, max = 100)
        String surname,

        @Schema(description = "Электронная почта клиента", example = "ivan@example.com")
        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @Schema(description = "Номер телефона клиента", example = "+79991234567")
        @NotBlank
        @Pattern(regexp = "\\+?[0-9]+",
                message = "Номер должен содержать только цифры и ведущий '+'")
        @Size(max = 30)
        String phone
) {
}
