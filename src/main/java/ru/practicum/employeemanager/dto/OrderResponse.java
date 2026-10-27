package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.practicum.employeemanager.model.Status;

import java.time.Instant;

@Schema(description = "Краткая информация о заказе для отображения в списках")
public record OrderResponse(
        @Schema(description = "Уникальный идентификатор заказа", example = "1")
        Long id,

        @Schema(description = "Уникальный идентификатор клиента, оформившего заказ", example = "123")
        Long customerId,

        @Schema(description = "Дата и время создания заказа", example = "2026-09-02T10:30:00Z")
        Instant createdAt,

        @Schema(description = "Текущий статус заказа", example = "NEW")
        Status status
) {
}
