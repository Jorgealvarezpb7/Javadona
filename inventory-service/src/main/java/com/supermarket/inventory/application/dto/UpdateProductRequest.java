package com.supermarket.inventory.application.dto;

import com.supermarket.inventory.domain.model.ProductCategory;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        String description,
        ProductCategory category,
        @Pattern(regexp = "\\d{13}") String barcodeValue,
        @Positive BigDecimal basePrice
) {
}
