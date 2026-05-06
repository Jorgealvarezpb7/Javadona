package com.supermarket.inventory.infrastructure.persistence.mapper;

import com.supermarket.inventory.domain.model.Barcode;
import com.supermarket.inventory.domain.model.Product;
import com.supermarket.inventory.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceMapper {

    public ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setCategory(product.getCategory());
        entity.setBarcodeValue(product.getBarcode().value());
        entity.setBasePrice(product.getBasePrice());
        entity.setActive(product.isActive());
        return entity;
    }

    public Product toDomain(ProductEntity entity) {
        return Product.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCategory(),
                new Barcode(entity.getBarcodeValue()),
                entity.getBasePrice(),
                entity.isActive()
        );
    }
}
