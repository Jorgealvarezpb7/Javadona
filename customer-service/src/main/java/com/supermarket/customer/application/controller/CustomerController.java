package com.supermarket.customer.application.controller;

import com.supermarket.customer.application.dto.CreateCustomerRequest;
import com.supermarket.customer.application.dto.CustomerResponse;
import com.supermarket.customer.application.dto.FrequentSalesPointsResponse;
import com.supermarket.customer.application.dto.RewardPointsRequest;
import com.supermarket.customer.application.dto.RewardPointsResponse;
import com.supermarket.customer.application.dto.UpdateCustomerRequest;
import com.supermarket.customer.application.mapper.CustomerMapper;
import com.supermarket.customer.domain.model.Customer;
import com.supermarket.customer.domain.model.DocumentType;
import com.supermarket.customer.domain.service.CustomerDomainService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerDomainService customerDomainService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerDomainService customerDomainService, CustomerMapper customerMapper) {
        this.customerDomainService = customerDomainService;
        this.customerMapper = customerMapper;
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> getAllCustomers(Pageable pageable) {
        Page<CustomerResponse> page = customerDomainService.findAll(pageable)
                .map(customerMapper::toResponse);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id) {
        Customer customer = customerDomainService.findById(id);
        return ResponseEntity.ok(customerMapper.toResponse(customer));
    }

    @GetMapping("/document/{type}/{value}")
    public ResponseEntity<CustomerResponse> getCustomerByDocument(
            @PathVariable DocumentType type,
            @PathVariable String value) {
        Customer customer = customerDomainService.findByDocument(type, value);
        return ResponseEntity.ok(customerMapper.toResponse(customer));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {
        Customer customer = customerDomainService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerMapper.toResponse(customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable UUID id,
            @RequestBody UpdateCustomerRequest request) {
        Customer customer = customerDomainService.updateCustomer(id, request);
        return ResponseEntity.ok(customerMapper.toResponse(customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerDomainService.softDeleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/rewards")
    public ResponseEntity<RewardPointsResponse> getRewardPoints(@PathVariable UUID id) {
        Customer customer = customerDomainService.findById(id);
        RewardPointsResponse response = new RewardPointsResponse(customer.getId(), customer.getRewardPoints());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/rewards")
    public ResponseEntity<RewardPointsResponse> addRewardPoints(
            @PathVariable UUID id,
            @Valid @RequestBody RewardPointsRequest request) {
        Customer customer = customerDomainService.addRewardPoints(id, request.getPoints());
        RewardPointsResponse response = new RewardPointsResponse(customer.getId(), customer.getRewardPoints());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/sales-points")
    public ResponseEntity<FrequentSalesPointsResponse> getFrequentSalesPoints(@PathVariable UUID id) {
        Customer customer = customerDomainService.findById(id);
        FrequentSalesPointsResponse response = new FrequentSalesPointsResponse(
                customer.getId(), customer.getFrequentSalesPoints());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/sales-points/{spId}")
    public ResponseEntity<Void> addFrequentSalesPoint(
            @PathVariable UUID id,
            @PathVariable UUID spId) {
        customerDomainService.addFrequentSalesPoint(id, spId);
        return ResponseEntity.noContent().build();
    }
}
