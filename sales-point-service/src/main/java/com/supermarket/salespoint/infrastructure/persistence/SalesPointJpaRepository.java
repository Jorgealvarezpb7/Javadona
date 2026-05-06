package com.supermarket.salespoint.infrastructure.persistence;

import com.supermarket.salespoint.infrastructure.persistence.entity.SalesPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SalesPointJpaRepository extends JpaRepository<SalesPointEntity, UUID> {
    List<SalesPointEntity> findByCityIgnoreCase(String city);
}
