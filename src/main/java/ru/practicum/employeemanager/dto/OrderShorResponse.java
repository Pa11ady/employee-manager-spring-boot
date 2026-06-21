package ru.practicum.employeemanager.dto;

import ru.practicum.employeemanager.model.Status;

import java.time.Instant;

public record OrderShorResponse(
        Long id,
        Instant createdAt,
        Status status
) {
}