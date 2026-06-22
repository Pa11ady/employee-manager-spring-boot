package ru.practicum.employeemanager.dto;

import ru.practicum.employeemanager.model.Status;

import java.time.Instant;

public record OrderShortResponse(
        Long id,
        Instant createdAt,
        Status status
) {
}