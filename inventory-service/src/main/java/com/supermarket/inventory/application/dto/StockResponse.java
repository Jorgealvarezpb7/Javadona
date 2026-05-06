package com.supermarket.inventory.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockResponse(
        UUID id,
        UUID salesPointId,
        UUID productId,
        int currentStock,
        int minimumStock,
        LocalDateTime lastUpdated
) {
}
