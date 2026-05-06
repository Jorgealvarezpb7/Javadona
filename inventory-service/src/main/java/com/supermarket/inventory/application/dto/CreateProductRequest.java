package com.supermarket.inventory.application.dto;

import com.supermarket.inventory.domain.model.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank String name,
        String description,
        @NotNull ProductCategory category,
        @NotBlank @Pattern(regexp = "\\d{13}") String barcodeValue,
        @NotNull @Positive BigDecimal basePrice
) {
}
