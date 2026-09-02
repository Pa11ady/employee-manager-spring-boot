package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Стандартный формат ответа при возникновении ошибки в API")
public record ErrorResponse(
        @Schema(description = "HTTP-статус ошибки", example = "400")
        int statusCode,

        @Schema(description = "Подробное сообщение об ошибке",
                example = "Некорректный формат запроса. Проверьте типы данных.")
        String message,

        @Schema(description = "Время возникновения ошибки", example = "2026-09-02T17:56:31.6897464")
        LocalDateTime timestamp
) {
}
