package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.practicum.employeemanager.model.Status;

import java.time.Instant;
import java.util.List;

@Schema(description = "Полная информация о заказе с данными клиента и списком позиций")
public record OrderFullResponse(
        @Schema(description = "Уникальный идентификатор заказа", example = "1")
        Long id,

        @Schema(description = "Данные клиента, оформившего заказ")
        CustomerResponse customer,

        @Schema(description = "Дата и время создания заказа", example = "2026-09-02T10:30:00Z")
        Instant createdAt,

        @Schema(description = "Текущий статус заказа", example = "NEW")
        Status status,

        @Schema(description = "Список позиций (товаров) в заказе")
        List<OrderItemSummary> items
) {
}
