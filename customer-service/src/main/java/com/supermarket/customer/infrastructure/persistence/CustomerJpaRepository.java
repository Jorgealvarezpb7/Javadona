package com.supermarket.customer.infrastructure.persistence;

import com.supermarket.customer.domain.model.DocumentType;
import com.supermarket.customer.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

    Optional<CustomerEntity> findByEmail(String email);

    Optional<CustomerEntity> findByDocumentTypeAndDocumentValue(DocumentType documentType, String documentValue);

    boolean existsByEmail(String email);

    boolean existsByDocumentTypeAndDocumentValue(DocumentType documentType, String documentValue);
}
