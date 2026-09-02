package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.practicum.employeemanager.model.Status;

@Schema(description = "Данные для обновления заказа (изменение статуса)")
public record UpdateOrderRequest(
        @Schema(description = "Новый статус заказа", example = "IN_PROGRESS")
        @NotNull
        Status status
) {
}
