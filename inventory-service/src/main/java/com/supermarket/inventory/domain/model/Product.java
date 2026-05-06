package com.supermarket.inventory.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

    private UUID id;
    private String name;
    private String description;
    private ProductCategory category;
    private Barcode barcode;
    private BigDecimal basePrice;
    private boolean active;

    private Product() {
    }

    public static Product create(String name, String description, ProductCategory category,
                                  Barcode barcode, BigDecimal basePrice) {
        Product p = new Product();
        p.id = UUID.randomUUID();
        p.name = name;
        p.description = description;
        p.category = category;
        p.barcode = barcode;
        p.basePrice = basePrice;
        p.active = true;
        return p;
    }

    public void update(String name, String description, ProductCategory category, BigDecimal basePrice) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.basePrice = basePrice;
    }

    public void deactivate() {
        this.active = false;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public Barcode getBarcode() {
        return barcode;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public boolean isActive() {
        return active;
    }

    // Static factory for reconstruction from persistence (used by persistence mapper)
    public static Product reconstitute(UUID id, String name, String description, ProductCategory category,
                                        Barcode barcode, BigDecimal basePrice, boolean active) {
        Product p = new Product();
        p.id = id;
        p.name = name;
        p.description = description;
        p.category = category;
        p.barcode = barcode;
        p.basePrice = basePrice;
        p.active = active;
        return p;
    }
}
