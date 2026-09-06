package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.practicum.employeemanager.model.Status;

@Schema(description = "Данные для создания нового заказа")
public record OrderRequest(
        @Schema(description = "Статус заказа при создании", example = "NEW")
        @NotNull
        Status status,

        @Schema(description = "Уникальный идентификатор клиента, оформившего заказ", example = "123")
        @NotNull
        Long customerId
) {
}
