package com.supermarket.sales.application.controller;

import com.supermarket.sales.application.dto.CreateSaleRequest;
import com.supermarket.sales.application.dto.SaleLineResponse;
import com.supermarket.sales.application.dto.SaleResponse;
import com.supermarket.sales.domain.model.Sale;
import com.supermarket.sales.domain.service.SaleDomainService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {

    private final SaleDomainService saleDomainService;

    public SaleController(SaleDomainService saleDomainService) {
        this.saleDomainService = saleDomainService;
    }

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody CreateSaleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saleDomainService.createSale(request)));
    }

    @GetMapping
    public ResponseEntity<Page<SaleResponse>> getAllSales(Pageable pageable) {
        return ResponseEntity.ok(saleDomainService.findAll(pageable).map(this::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getSaleById(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(saleDomainService.findById(id)));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<SaleResponse>> getSalesByCustomer(
            @PathVariable UUID customerId, Pageable pageable) {
        return ResponseEntity.ok(saleDomainService.findByCustomerId(customerId, pageable).map(this::toResponse));
    }

    @GetMapping("/sales-point/{salesPointId}")
    public ResponseEntity<Page<SaleResponse>> getSalesBySalesPoint(
            @PathVariable UUID salesPointId, Pageable pageable) {
        return ResponseEntity.ok(saleDomainService.findBySalesPointId(salesPointId, pageable).map(this::toResponse));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<SaleResponse> refundSale(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(saleDomainService.refundSale(id)));
    }

    private SaleResponse toResponse(Sale sale) {
        var lines = sale.getLines().stream()
                .map(l -> new SaleLineResponse(
                        l.getId(), l.getProductId(), l.getProductName(),
                        l.getQuantity(), l.getUnitPrice(), l.getSubTotal()))
                .toList();
        return new SaleResponse(
                sale.getId(), sale.getCustomerId(), sale.getSalesPointId(),
                sale.getSaleDate(), lines, sale.getTotalAmount(),
                sale.getPaymentMethod(), sale.getStatus());
    }
}
