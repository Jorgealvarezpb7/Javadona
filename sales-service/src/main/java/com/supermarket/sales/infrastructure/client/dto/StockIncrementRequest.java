package com.supermarket.sales.infrastructure.client.dto;

import java.util.List;
import java.util.UUID;

public record StockIncrementRequest(
        UUID salesPointId,
        List<StockLineItem> lines
) {
    public record StockLineItem(
            UUID productId,
            int quantity
    ) {
    }
}
