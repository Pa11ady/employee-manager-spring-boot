package ru.practicum.employeemanager.dto;

import ru.practicum.employeemanager.model.Status;

import java.time.Instant;

public record OrderResponse(
        Long id,
        Long customerId,
        Instant createdAt,
        Status status
) {
}