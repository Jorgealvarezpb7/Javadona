package com.supermarket.inventory.infrastructure.exception;

import java.util.UUID;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(UUID productId, int available, int requested) {
        super(String.format("Insufficient stock for product %s: available=%d, requested=%d",
                productId, available, requested));
    }

    public InsufficientStockException(String message) {
        super(message);
    }
}
