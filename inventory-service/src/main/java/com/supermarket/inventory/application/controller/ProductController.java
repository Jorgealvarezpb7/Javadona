package com.supermarket.inventory.application.controller;

import com.supermarket.inventory.application.dto.CreateProductRequest;
import com.supermarket.inventory.application.dto.ProductResponse;
import com.supermarket.inventory.application.dto.UpdateProductRequest;
import com.supermarket.inventory.application.mapper.InventoryMapper;
import com.supermarket.inventory.domain.model.Barcode;
import com.supermarket.inventory.domain.model.Product;
import com.supermarket.inventory.domain.repository.ProductRepository;
import com.supermarket.inventory.infrastructure.exception.ProductNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final InventoryMapper inventoryMapper;

    public ProductController(ProductRepository productRepository, InventoryMapper inventoryMapper) {
        this.productRepository = productRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @GetMapping
    public Page<ProductResponse> listProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(inventoryMapper::toProductResponse);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable UUID id) {
        return productRepository.findById(id)
                .map(inventoryMapper::toProductResponse)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @GetMapping("/barcode/{ean}")
    public ProductResponse getProductByBarcode(@PathVariable String ean) {
        return productRepository.findByBarcode(ean)
                .map(inventoryMapper::toProductResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with barcode: " + ean));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = Product.create(
                request.name(),
                request.description(),
                request.category(),
                new Barcode(request.barcodeValue()),
                request.basePrice()
        );
        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryMapper.toProductResponse(saved));
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable UUID id,
                                          @Valid @RequestBody UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.update(
                request.name() != null ? request.name() : product.getName(),
                request.description() != null ? request.description() : product.getDescription(),
                request.category() != null ? request.category() : product.getCategory(),
                request.basePrice() != null ? request.basePrice() : product.getBasePrice()
        );

        Product saved = productRepository.save(product);
        return inventoryMapper.toProductResponse(saved);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateProduct(@PathVariable UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.deactivate();
        productRepository.save(product);
        return ResponseEntity.noContent().build();
    }
}
