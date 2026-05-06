package com.supermarket.salespoint.domain.service;

import com.supermarket.salespoint.domain.model.Address;
import com.supermarket.salespoint.domain.model.OpeningHours;
import com.supermarket.salespoint.domain.model.SalesPoint;
import com.supermarket.salespoint.domain.model.SalesPointStatus;
import com.supermarket.salespoint.domain.repository.SalesPointRepository;
import com.supermarket.salespoint.infrastructure.exception.SalesPointNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SalesPointDomainService {

    private final SalesPointRepository salesPointRepository;

    public SalesPointDomainService(SalesPointRepository salesPointRepository) {
        this.salesPointRepository = salesPointRepository;
    }

    public SalesPoint createSalesPoint(String name, Address address, String phoneNumber, OpeningHours openingHours) {
        SalesPoint salesPoint = SalesPoint.create(name, address, phoneNumber, openingHours);
        return salesPointRepository.save(salesPoint);
    }

    public SalesPoint updateSalesPoint(UUID id, String name, Address address, String phoneNumber, OpeningHours openingHours) {
        SalesPoint salesPoint = salesPointRepository.findById(id)
                .orElseThrow(() -> new SalesPointNotFoundException(id));
        salesPoint.update(name, address, phoneNumber, openingHours);
        return salesPointRepository.save(salesPoint);
    }

    public SalesPoint changeStatus(UUID id, SalesPointStatus status) {
        SalesPoint salesPoint = salesPointRepository.findById(id)
                .orElseThrow(() -> new SalesPointNotFoundException(id));
        salesPoint.changeStatus(status);
        return salesPointRepository.save(salesPoint);
    }

    public void deleteSalesPoint(UUID id) {
        salesPointRepository.findById(id)
                .orElseThrow(() -> new SalesPointNotFoundException(id));
        salesPointRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SalesPoint getSalesPoint(UUID id) {
        return salesPointRepository.findById(id)
                .orElseThrow(() -> new SalesPointNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<SalesPoint> listAll(Pageable pageable) {
        return salesPointRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<SalesPoint> findByCity(String city) {
        return salesPointRepository.findByCity(city);
    }
}
