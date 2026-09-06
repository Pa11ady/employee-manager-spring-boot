package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Данные для добавления или обновления позиции (товара) в заказе")
public record OrderItemRequest(
        @Schema(description = "Уникальный идентификатор товара", example = "10")
        @NotNull(message = "ID товара обязателен")
        Long productId,

        @Schema(description = "Количество товара в заказе", example = "2")
        @NotNull(message = "Количество обязательно")
        @Positive(message = "Количество должно быть больше нуля")
        Integer quantity
) {
}
