package ru.practicum.employeemanager.dto;

import jakarta.validation.constraints.*;

public record CustomerRequest(
        @NotBlank
        @Size(min = 1, max = 100)
        String name,

        @NotBlank
        @Size(min = 1, max = 100)
        String surname,

        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotBlank
        @Pattern(regexp = "\\+?[0-9]+",
                message = "Номер должен содержать только цифры и ведущий '+'")
        @Size(max = 30)
        String phone
) {
}
