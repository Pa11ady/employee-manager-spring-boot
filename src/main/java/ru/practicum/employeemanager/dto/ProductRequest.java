package ru.practicum.employeemanager.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Название товара не может быть пустым")
        @Size(max = 255, message = "Название товара не может превышать 255 символов")
        String name,

        @Size(max = 511, message = "Описание товара не может превышать 511 символов")
        String description,

        @NotNull(message = "Цена товара обязательна")
        @DecimalMin(value = "0.01", message = "Цена товара должна быть больше нуля")
        BigDecimal price
) {
}