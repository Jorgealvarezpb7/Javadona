package com.supermarket.inventory.infrastructure.persistence;

import com.supermarket.inventory.infrastructure.persistence.entity.StoreInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreInventoryJpaRepository extends JpaRepository<StoreInventoryEntity, UUID> {

    List<StoreInventoryEntity> findBySalesPointId(UUID salesPointId);

    Optional<StoreInventoryEntity> findBySalesPointIdAndProductId(UUID salesPointId, UUID productId);

    List<StoreInventoryEntity> findBySalesPointIdAndProductIdIn(UUID salesPointId, List<UUID> productIds);

    @Query("SELECT si FROM StoreInventoryEntity si WHERE si.salesPointId = :sid AND si.currentStock < si.minimumStock")
    List<StoreInventoryEntity> findLowStockBySalesPointId(@Param("sid") UUID salesPointId);
}
