package com.supermarket.sales.infrastructure.persistence;

import com.supermarket.sales.domain.model.Sale;
import com.supermarket.sales.domain.repository.SaleRepository;
import com.supermarket.sales.infrastructure.persistence.mapper.SalePersistenceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SaleRepositoryImpl implements SaleRepository {

    private final SaleJpaRepository jpaRepository;
    private final SalePersistenceMapper mapper;

    public SaleRepositoryImpl(SaleJpaRepository jpaRepository, SalePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Sale save(Sale sale) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(sale)));
    }

    @Override
    public Optional<Sale> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Sale> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Sale> findByCustomerId(UUID customerId, Pageable pageable) {
        return jpaRepository.findByCustomerId(customerId, pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Sale> findBySalesPointId(UUID salesPointId, Pageable pageable) {
        return jpaRepository.findBySalesPointId(salesPointId, pageable).map(mapper::toDomain);
    }
}
