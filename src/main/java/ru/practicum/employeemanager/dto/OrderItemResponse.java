package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация о позиции (товаре) в заказе")
public record OrderItemResponse(
        @Schema(description = "Уникальный идентификатор позиции в заказе", example = "100")
        Long id,

        @Schema(description = "Уникальный идентификатор заказа, к которому относится позиция", example = "1")
        Long orderId,

        @Schema(description = "Уникальный идентификатор товара", example = "10")
        Long productId,

        @Schema(description = "Количество товара в позиции заказа", example = "2")
        Integer quantity
) {
}
