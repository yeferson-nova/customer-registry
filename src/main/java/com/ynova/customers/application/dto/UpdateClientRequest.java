package com.ynova.customers.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateClientRequest(
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    String name,

    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    LocalDate birthDate,

    @NotNull(message = "Gender ID cannot be null")
    Long genderId,

    @NotBlank(message = "Account number cannot be blank")
    @Pattern(regexp = "[0-9]{12,15}", message = "Account number must be between 12 and 15 digits")
    String numCTA
) {}
