package com.supermarket.salespoint.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record CreateSalesPointRequest(
        @NotBlank String name,
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String postalCode,
        @NotBlank String province,
        @NotBlank String phoneNumber,
        @NotNull LocalTime opensAt,
        @NotNull LocalTime closesAt
) {
}
