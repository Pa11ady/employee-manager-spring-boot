package ru.practicum.employeemanager.dto;

import ru.practicum.employeemanager.model.Status;

import java.time.Instant;

public record OrderWithCustomerResponse(
        Long id,
        CustomerResponse customer,
        Instant createdAt,
        Status status
) {
}