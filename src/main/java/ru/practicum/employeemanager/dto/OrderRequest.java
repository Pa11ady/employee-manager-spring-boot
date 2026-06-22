package ru.practicum.employeemanager.dto;

import jakarta.validation.constraints.NotNull;
import ru.practicum.employeemanager.model.Status;

public record OrderRequest(
        @NotNull
        Status status,

        @NotNull
        Long customerId
) {
}