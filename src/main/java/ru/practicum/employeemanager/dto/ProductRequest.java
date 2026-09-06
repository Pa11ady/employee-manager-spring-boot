package ru.practicum.employeemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Данные для создания нового товара")
public record ProductRequest(
        @Schema(description = "Название товара", example = "Ноутбук Lenovo IdeaPad")
        @NotBlank(message = "Название товара не может быть пустым")
        @Size(max = 255, message = "Название товара не может превышать 255 символов")
        String name,

        @Schema(description = "Описание товара", example = "15.6 дюймов, 16 ГБ RAM, 512 ГБ SSD")
        String description,

        @Schema(description = "Цена товара в рублях", example = "59999.99")
        @NotNull(message = "Цена товара обязательна")
        @DecimalMin(value = "0.01", message = "Цена товара должна быть больше нуля")
        BigDecimal price
) {
}
