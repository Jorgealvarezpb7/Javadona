package com.supermarket.inventory.application.dto;

import java.util.List;
import java.util.UUID;

public record StockDecrementRequest(
        UUID salesPointId,
        List<StockLineItem> lines
) {
    public record StockLineItem(UUID productId, int quantity) {
    }
}
