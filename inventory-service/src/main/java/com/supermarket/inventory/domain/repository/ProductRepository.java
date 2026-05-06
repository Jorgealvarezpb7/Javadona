package com.supermarket.inventory.domain.repository;

import com.supermarket.inventory.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    Optional<Product> findByBarcode(String barcodeValue);

    Page<Product> findAll(Pageable pageable);

    boolean existsByBarcode(String barcodeValue);
}
