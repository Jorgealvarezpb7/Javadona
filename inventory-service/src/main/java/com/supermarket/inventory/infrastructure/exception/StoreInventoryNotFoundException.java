package com.supermarket.inventory.infrastructure.exception;

import java.util.UUID;

public class StoreInventoryNotFoundException extends RuntimeException {

    public StoreInventoryNotFoundException(UUID salesPointId, UUID productId) {
        super(String.format("Store inventory not found for salesPointId=%s, productId=%s",
                salesPointId, productId));
    }

    public StoreInventoryNotFoundException(String message) {
        super(message);
    }
}
