package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Информация о товаре в ответах API")
public record ProductResponse(
        @Schema(description = "Уникальный идентификатор товара", example = "10")
        Long id,

        @Schema(description = "Название товара", example = "Ноутбук Lenovo IdeaPad")
        String name,

        @Schema(description = "Описание товара", example = "15.6 дюймов, 16 ГБ RAM, 512 ГБ SSD")
        String description,

        @Schema(description = "Цена товара в рублях", example = "59999.99")
        BigDecimal price
) {
}
