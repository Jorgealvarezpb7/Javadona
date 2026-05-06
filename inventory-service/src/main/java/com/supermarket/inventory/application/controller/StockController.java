package com.supermarket.inventory.application.controller;

import com.supermarket.inventory.application.dto.StockAdjustmentRequest;
import com.supermarket.inventory.application.dto.StockDecrementRequest;
import com.supermarket.inventory.application.dto.StockIncrementRequest;
import com.supermarket.inventory.application.dto.StockResponse;
import com.supermarket.inventory.application.mapper.InventoryMapper;
import com.supermarket.inventory.domain.model.StoreInventory;
import com.supermarket.inventory.domain.repository.StoreInventoryRepository;
import com.supermarket.inventory.domain.service.StockDomainService;
import com.supermarket.inventory.infrastructure.exception.StoreInventoryNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/inventory/stock")
public class StockController {

    private final StoreInventoryRepository storeInventoryRepository;
    private final StockDomainService stockDomainService;
    private final InventoryMapper inventoryMapper;

    public StockController(StoreInventoryRepository storeInventoryRepository,
                            StockDomainService stockDomainService,
                            InventoryMapper inventoryMapper) {
        this.storeInventoryRepository = storeInventoryRepository;
        this.stockDomainService = stockDomainService;
        this.inventoryMapper = inventoryMapper;
    }

    @GetMapping("/{salesPointId}")
    public List<StockResponse> getStockForSalesPoint(@PathVariable UUID salesPointId) {
        return storeInventoryRepository.findBySalesPointId(salesPointId).stream()
                .map(inventoryMapper::toStockResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{salesPointId}/{productId}")
    public StockResponse getStockForProduct(@PathVariable UUID salesPointId,
                                             @PathVariable UUID productId) {
        return storeInventoryRepository.findBySalesPointIdAndProductId(salesPointId, productId)
                .map(inventoryMapper::toStockResponse)
                .orElseThrow(() -> new StoreInventoryNotFoundException(salesPointId, productId));
    }

    @PutMapping("/{salesPointId}/{productId}")
    public StockResponse adjustStock(@PathVariable UUID salesPointId,
                                      @PathVariable UUID productId,
                                      @Valid @RequestBody StockAdjustmentRequest request) {
        StoreInventory inventory = storeInventoryRepository
                .findBySalesPointIdAndProductId(salesPointId, productId)
                .orElseGet(() -> StoreInventory.create(salesPointId, productId, request.newStock(), request.minimumStock()));

        // Update existing or newly created inventory with requested values
        inventory.adjust(request.newStock());
        inventory.adjustMinimumStock(request.minimumStock());

        StoreInventory saved = storeInventoryRepository.save(inventory);
        return inventoryMapper.toStockResponse(saved);
    }

    @GetMapping("/{salesPointId}/low-stock")
    public List<StockResponse> getLowStockItems(@PathVariable UUID salesPointId) {
        return storeInventoryRepository
                .findBySalesPointIdAndCurrentStockLessThanMinimumStock(salesPointId).stream()
                .map(inventoryMapper::toStockResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/decrement")
    public ResponseEntity<Void> decrementStock(@RequestBody StockDecrementRequest request) {
        stockDomainService.decrementStock(request.salesPointId(), request.lines());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/increment")
    public ResponseEntity<Void> incrementStock(@RequestBody StockIncrementRequest request) {
        stockDomainService.incrementStock(request.salesPointId(), request.lines());
        return ResponseEntity.noContent().build();
    }
}
