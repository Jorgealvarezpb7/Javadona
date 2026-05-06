package com.supermarket.inventory.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class StoreInventory {

    private UUID id;
    private UUID salesPointId;
    private UUID productId;
    private int currentStock;
    private int minimumStock;
    private LocalDateTime lastUpdated;

    private StoreInventory() {
    }

    public static StoreInventory create(UUID salesPointId, UUID productId, int initialStock, int minimumStock) {
        StoreInventory si = new StoreInventory();
        si.id = UUID.randomUUID();
        si.salesPointId = salesPointId;
        si.productId = productId;
        si.currentStock = initialStock;
        si.minimumStock = minimumStock;
        si.lastUpdated = LocalDateTime.now();
        return si;
    }

    public void decrementStock(int quantity) {
        if (currentStock < quantity) {
            throw new IllegalStateException(String.format(
                    "Insufficient stock for product %s: available=%d, requested=%d",
                    productId, currentStock, quantity));
        }
        this.currentStock -= quantity;
        this.lastUpdated = LocalDateTime.now();
    }

    public void incrementStock(int quantity) {
        this.currentStock += quantity;
        this.lastUpdated = LocalDateTime.now();
    }

    public void adjust(int newStock) {
        this.currentStock = newStock;
        this.lastUpdated = LocalDateTime.now();
    }

    public void adjustMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
        this.lastUpdated = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getSalesPointId() {
        return salesPointId;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    // Static factory for reconstruction from persistence (used by persistence mapper)
    public static StoreInventory reconstitute(UUID id, UUID salesPointId, UUID productId,
                                               int currentStock, int minimumStock, LocalDateTime lastUpdated) {
        StoreInventory si = new StoreInventory();
        si.id = id;
        si.salesPointId = salesPointId;
        si.productId = productId;
        si.currentStock = currentStock;
        si.minimumStock = minimumStock;
        si.lastUpdated = lastUpdated;
        return si;
    }
}
