package com.supermarket.inventory.domain.model;

import java.util.UUID;

public record StoreInventoryId(UUID value) {

    public StoreInventoryId {
        if (value == null) {
            throw new IllegalArgumentException("StoreInventoryId cannot be null");
        }
    }

    public static StoreInventoryId of(UUID value) {
        return new StoreInventoryId(value);
    }
}
