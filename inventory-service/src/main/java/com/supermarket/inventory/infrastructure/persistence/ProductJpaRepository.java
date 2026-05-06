package com.supermarket.inventory.infrastructure.persistence;

import com.supermarket.inventory.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findByBarcodeValue(String barcodeValue);

    boolean existsByBarcodeValue(String barcodeValue);
}
