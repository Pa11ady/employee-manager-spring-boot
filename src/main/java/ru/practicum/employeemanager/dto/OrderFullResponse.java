package ru.practicum.employeemanager.dto;

import ru.practicum.employeemanager.model.Status;

import java.time.Instant;
import java.util.List;

public record OrderFullResponse(
        Long id,
        CustomerResponse customer,
        Instant createdAt,
        Status status,

        List<OrderItemSummary> items
) {
}