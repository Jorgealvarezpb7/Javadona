package com.supermarket.inventory.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "store_inventories",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sales_point_id", "product_id"}))
@Getter
@Setter
@NoArgsConstructor
public class StoreInventoryEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "sales_point_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID salesPointId;

    @Column(name = "product_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID productId;

    @Column(name = "current_stock", nullable = false)
    private int currentStock;

    @Column(name = "minimum_stock", nullable = false)
    private int minimumStock;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
}
