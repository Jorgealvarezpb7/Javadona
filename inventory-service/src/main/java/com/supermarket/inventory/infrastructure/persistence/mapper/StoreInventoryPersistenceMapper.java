package com.supermarket.inventory.infrastructure.persistence.mapper;

import com.supermarket.inventory.domain.model.StoreInventory;
import com.supermarket.inventory.infrastructure.persistence.entity.StoreInventoryEntity;
import org.springframework.stereotype.Component;

@Component
public class StoreInventoryPersistenceMapper {

    public StoreInventoryEntity toEntity(StoreInventory inventory) {
        StoreInventoryEntity entity = new StoreInventoryEntity();
        entity.setId(inventory.getId());
        entity.setSalesPointId(inventory.getSalesPointId());
        entity.setProductId(inventory.getProductId());
        entity.setCurrentStock(inventory.getCurrentStock());
        entity.setMinimumStock(inventory.getMinimumStock());
        entity.setLastUpdated(inventory.getLastUpdated());
        return entity;
    }

    public StoreInventory toDomain(StoreInventoryEntity entity) {
        return StoreInventory.reconstitute(
                entity.getId(),
                entity.getSalesPointId(),
                entity.getProductId(),
                entity.getCurrentStock(),
                entity.getMinimumStock(),
                entity.getLastUpdated()
        );
    }
}
