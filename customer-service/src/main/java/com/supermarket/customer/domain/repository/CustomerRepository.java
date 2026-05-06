package com.supermarket.customer.domain.repository;

import com.supermarket.customer.domain.model.Customer;
import com.supermarket.customer.domain.model.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(UUID id);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByDocument(DocumentType type, String value);
    Page<Customer> findAll(Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByDocument(DocumentType type, String value);
}
