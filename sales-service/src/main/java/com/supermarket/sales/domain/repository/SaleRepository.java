package com.supermarket.sales.domain.repository;

import com.supermarket.sales.domain.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface SaleRepository {

    Sale save(Sale sale);

    Optional<Sale> findById(UUID id);

    Page<Sale> findAll(Pageable pageable);

    Page<Sale> findByCustomerId(UUID customerId, Pageable pageable);

    Page<Sale> findBySalesPointId(UUID salesPointId, Pageable pageable);
}
