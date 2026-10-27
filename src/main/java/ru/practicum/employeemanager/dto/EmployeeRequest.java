package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import ru.practicum.employeemanager.model.Role;

@Schema(description = "Данные для создания нового сотрудника")
public record EmployeeRequest(
        @Schema(description = "Имя сотрудника", example = "Петр")
        @NotBlank
        @Size(min = 1, max = 100)
        String name,

        @Schema(description = "Фамилия сотрудника", example = "Петров")
        @NotBlank
        @Size(min = 1, max = 100)
        String surname,

        @Schema(description = "Корпоративная электронная почта", example = "petr@company.com")
        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        // Допустим, что телефона может не быть
        @Schema(description = "Номер телефона сотрудника (необязательный)", example = "+79991234567")
        @Pattern(regexp = "\\+?[0-9]+",
                message = "Номер должен содержать только цифры и ведущий '+'")
        @Size(max = 30)
        String phone,

        @Schema(description = "Пароль для доступа к системе", example = "StrongP@ssw0rd123")
        @NotBlank
        @Size(min = 6, max = 64)
        String password,

        @Schema(description = "Роль сотрудника в системе", example = "MANAGER")
        @NotNull
        Role role
) {
}