package com.supermarket.inventory.infrastructure.persistence;

import com.supermarket.inventory.domain.model.Product;
import com.supermarket.inventory.domain.repository.ProductRepository;
import com.supermarket.inventory.infrastructure.persistence.entity.ProductEntity;
import com.supermarket.inventory.infrastructure.persistence.mapper.ProductPersistenceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductPersistenceMapper mapper;

    public ProductRepositoryImpl(ProductJpaRepository jpaRepository, ProductPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Product> findByBarcode(String barcodeValue) {
        return jpaRepository.findByBarcodeValue(barcodeValue).map(mapper::toDomain);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public boolean existsByBarcode(String barcodeValue) {
        return jpaRepository.existsByBarcodeValue(barcodeValue);
    }
}
