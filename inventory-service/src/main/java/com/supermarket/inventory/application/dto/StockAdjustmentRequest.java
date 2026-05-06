package com.supermarket.inventory.application.dto;

import jakarta.validation.constraints.Min;

public record StockAdjustmentRequest(
        @Min(0) int newStock,
        @Min(0) int minimumStock
) {
}
