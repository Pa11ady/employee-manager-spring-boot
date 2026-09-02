package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация о клиенте в ответах API")
public record CustomerResponse(
        @Schema(description = "Уникальный идентификатор клиента", example = "123")
        Long id,

        @Schema(description = "Имя клиента", example = "Иван")
        String name,

        @Schema(description = "Фамилия клиента", example = "Иванов")
        String surname,

        @Schema(description = "Электронная почта клиента", example = "ivan@example.com")
        String email,

        @Schema(description = "Номер телефона клиента", example = "+79991234567")
        String phone
) {
}
