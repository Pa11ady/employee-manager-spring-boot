package ru.practicum.employeemanager.dto;

public record OrderItemSummary(Long id, Long productId, String productName, Integer quantity) {
}