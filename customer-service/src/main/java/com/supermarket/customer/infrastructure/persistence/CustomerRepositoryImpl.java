package com.supermarket.customer.infrastructure.persistence;

import com.supermarket.customer.domain.model.Customer;
import com.supermarket.customer.domain.model.DocumentType;
import com.supermarket.customer.domain.repository.CustomerRepository;
import com.supermarket.customer.infrastructure.persistence.entity.CustomerEntity;
import com.supermarket.customer.infrastructure.persistence.mapper.CustomerPersistenceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;
    private final CustomerPersistenceMapper persistenceMapper;

    public CustomerRepositoryImpl(CustomerJpaRepository jpaRepository,
                                   CustomerPersistenceMapper persistenceMapper) {
        this.jpaRepository = jpaRepository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = persistenceMapper.toEntity(customer);
        CustomerEntity saved = jpaRepository.save(entity);
        return persistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public Optional<Customer> findByDocument(DocumentType type, String value) {
        return jpaRepository.findByDocumentTypeAndDocumentValue(type, value)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDocument(DocumentType type, String value) {
        return jpaRepository.existsByDocumentTypeAndDocumentValue(type, value);
    }
}
