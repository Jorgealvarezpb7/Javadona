package com.supermarket.sales.infrastructure.persistence;

import com.supermarket.sales.infrastructure.persistence.entity.SaleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleJpaRepository extends JpaRepository<SaleEntity, UUID> {
    Page<SaleEntity> findByCustomerId(UUID customerId, Pageable pageable);
    Page<SaleEntity> findBySalesPointId(UUID salesPointId, Pageable pageable);
}
