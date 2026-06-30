package ru.practicum.employeemanager.dto;

public record OrderItemResponse(
        Long id,
        Long orderId,
        Long productId,
        Integer quantity
) {
}