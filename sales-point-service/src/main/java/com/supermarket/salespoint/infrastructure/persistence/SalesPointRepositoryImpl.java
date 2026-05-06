package com.supermarket.salespoint.infrastructure.persistence;

import com.supermarket.salespoint.domain.model.SalesPoint;
import com.supermarket.salespoint.domain.repository.SalesPointRepository;
import com.supermarket.salespoint.infrastructure.persistence.entity.SalesPointEntity;
import com.supermarket.salespoint.infrastructure.persistence.mapper.SalesPointPersistenceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SalesPointRepositoryImpl implements SalesPointRepository {

    private final SalesPointJpaRepository jpaRepository;
    private final SalesPointPersistenceMapper persistenceMapper;

    public SalesPointRepositoryImpl(SalesPointJpaRepository jpaRepository,
                                    SalesPointPersistenceMapper persistenceMapper) {
        this.jpaRepository = jpaRepository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public SalesPoint save(SalesPoint salesPoint) {
        SalesPointEntity entity = persistenceMapper.toEntity(salesPoint);
        SalesPointEntity saved = jpaRepository.save(entity);
        return persistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<SalesPoint> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public Page<SalesPoint> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public List<SalesPoint> findByCity(String city) {
        return jpaRepository.findByCityIgnoreCase(city)
                .stream()
                .map(persistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
