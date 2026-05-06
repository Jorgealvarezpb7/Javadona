package com.supermarket.salespoint.domain.repository;

import com.supermarket.salespoint.domain.model.SalesPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalesPointRepository {
    SalesPoint save(SalesPoint salesPoint);
    Optional<SalesPoint> findById(UUID id);
    Page<SalesPoint> findAll(Pageable pageable);
    List<SalesPoint> findByCity(String city);
    void deleteById(UUID id);
}
