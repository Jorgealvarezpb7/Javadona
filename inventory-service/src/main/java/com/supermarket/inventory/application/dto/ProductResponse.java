package com.supermarket.inventory.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        String category,
        String barcodeValue,
        BigDecimal basePrice,
        boolean active
) {
}
