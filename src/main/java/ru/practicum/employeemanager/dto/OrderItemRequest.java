package ru.practicum.employeemanager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
        @NotNull(message = "ID товара обязателен")
        Long productId,

        @NotNull(message = "Количество обязательно")
        @Positive(message = "Количество должно быть больше нуля")
        Integer quantity

) {
}