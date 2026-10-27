package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Краткая информация о позиции (товаре) в заказе для отображения в составе полного ответа")
public record OrderItemSummary(
        @Schema(description = "Уникальный идентификатор позиции в заказе", example = "100")
        Long id,

        @Schema(description = "Уникальный идентификатор товара", example = "10")
        Long productId,

        @Schema(description = "Название товара", example = "Ноутбук Lenovo IdeaPad")
        String productName,

        @Schema(description = "Количество товара в позиции заказа", example = "2")
        Integer quantity
) {
}
