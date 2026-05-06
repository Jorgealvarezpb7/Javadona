package com.supermarket.inventory.domain.service;

import com.supermarket.inventory.application.dto.StockDecrementRequest;
import com.supermarket.inventory.application.dto.StockIncrementRequest;
import com.supermarket.inventory.domain.model.StoreInventory;
import com.supermarket.inventory.domain.repository.StoreInventoryRepository;
import com.supermarket.inventory.infrastructure.exception.InsufficientStockException;
import com.supermarket.inventory.infrastructure.exception.StoreInventoryNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StockDomainService {

    private final StoreInventoryRepository storeInventoryRepository;

    public StockDomainService(StoreInventoryRepository storeInventoryRepository) {
        this.storeInventoryRepository = storeInventoryRepository;
    }

    @Transactional
    public void decrementStock(UUID salesPointId, List<StockDecrementRequest.StockLineItem> lines) {
        List<UUID> productIds = lines.stream()
                .map(StockDecrementRequest.StockLineItem::productId)
                .collect(Collectors.toList());

        List<StoreInventory> inventories = storeInventoryRepository
                .findBySalesPointIdAndProductIdIn(salesPointId, productIds);

        Map<UUID, StoreInventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(StoreInventory::getProductId, i -> i));

        // Step 1: Validate ALL lines before modifying any (atomic validation)
        for (StockDecrementRequest.StockLineItem line : lines) {
            StoreInventory inventory = inventoryMap.get(line.productId());
            if (inventory == null) {
                throw new StoreInventoryNotFoundException(salesPointId, line.productId());
            }
            if (inventory.getCurrentStock() < line.quantity()) {
                throw new InsufficientStockException(line.productId(),
                        inventory.getCurrentStock(), line.quantity());
            }
        }

        // Step 2: Apply decrements only after full validation passes
        for (StockDecrementRequest.StockLineItem line : lines) {
            StoreInventory inventory = inventoryMap.get(line.productId());
            inventory.decrementStock(line.quantity());
            storeInventoryRepository.save(inventory);
        }
    }

    @Transactional
    public void incrementStock(UUID salesPointId, List<StockIncrementRequest.StockLineItem> lines) {
        List<UUID> productIds = lines.stream()
                .map(StockIncrementRequest.StockLineItem::productId)
                .collect(Collectors.toList());

        List<StoreInventory> inventories = storeInventoryRepository
                .findBySalesPointIdAndProductIdIn(salesPointId, productIds);

        Map<UUID, StoreInventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(StoreInventory::getProductId, i -> i));

        for (StockIncrementRequest.StockLineItem line : lines) {
            StoreInventory inventory = inventoryMap.get(line.productId());
            if (inventory == null) {
                throw new StoreInventoryNotFoundException(salesPointId, line.productId());
            }
            inventory.incrementStock(line.quantity());
            storeInventoryRepository.save(inventory);
        }
    }
}
