package com.supermarket.inventory.infrastructure.persistence;

import com.supermarket.inventory.domain.model.StoreInventory;
import com.supermarket.inventory.domain.repository.StoreInventoryRepository;
import com.supermarket.inventory.infrastructure.persistence.entity.StoreInventoryEntity;
import com.supermarket.inventory.infrastructure.persistence.mapper.StoreInventoryPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class StoreInventoryRepositoryImpl implements StoreInventoryRepository {

    private final StoreInventoryJpaRepository jpaRepository;
    private final StoreInventoryPersistenceMapper mapper;

    public StoreInventoryRepositoryImpl(StoreInventoryJpaRepository jpaRepository,
                                         StoreInventoryPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public StoreInventory save(StoreInventory inventory) {
        StoreInventoryEntity entity = mapper.toEntity(inventory);
        StoreInventoryEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<StoreInventory> findBySalesPointIdAndProductId(UUID salesPointId, UUID productId) {
        return jpaRepository.findBySalesPointIdAndProductId(salesPointId, productId)
                .map(mapper::toDomain);
    }

    @Override
    public List<StoreInventory> findBySalesPointId(UUID salesPointId) {
        return jpaRepository.findBySalesPointId(salesPointId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoreInventory> findBySalesPointIdAndCurrentStockLessThanMinimumStock(UUID salesPointId) {
        return jpaRepository.findLowStockBySalesPointId(salesPointId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoreInventory> findBySalesPointIdAndProductIdIn(UUID salesPointId, List<UUID> productIds) {
        return jpaRepository.findBySalesPointIdAndProductIdIn(salesPointId, productIds).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
