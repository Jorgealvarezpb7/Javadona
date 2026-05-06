package com.supermarket.sales.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record SaleLineRequest(
        @NotNull UUID productId,
        @NotBlank String productName,
        @Min(1) int quantity,
        @NotNull @Positive BigDecimal unitPrice
) {
}
