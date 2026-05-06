package com.supermarket.inventory.domain.repository;

import com.supermarket.inventory.domain.model.StoreInventory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreInventoryRepository {

    StoreInventory save(StoreInventory inventory);

    Optional<StoreInventory> findBySalesPointIdAndProductId(UUID salesPointId, UUID productId);

    List<StoreInventory> findBySalesPointId(UUID salesPointId);

    List<StoreInventory> findBySalesPointIdAndCurrentStockLessThanMinimumStock(UUID salesPointId);

    List<StoreInventory> findBySalesPointIdAndProductIdIn(UUID salesPointId, List<UUID> productIds);
}
