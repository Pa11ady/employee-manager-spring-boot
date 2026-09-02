package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.practicum.employeemanager.model.Role;

@Schema(description = "Информация о сотруднике в ответах API")
public record EmployeeResponse(
        @Schema(description = "Уникальный идентификатор сотрудника", example = "123")
        Long id,

        @Schema(description = "Имя сотрудника", example = "Петр")
        String name,

        @Schema(description = "Фамилия сотрудника", example = "Петров")
        String surname,

        @Schema(description = "Электронная почта сотрудника", example = "petr@company.com")
        String email,

        @Schema(description = "Номер телефона сотрудника", example = "+79991234567")
        String phone,

        @Schema(description = "Роль сотрудника в системе", example = "MANAGER")
        Role role
) {
}
