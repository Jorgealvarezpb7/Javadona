package com.supermarket.salespoint.application.controller;

import com.supermarket.salespoint.application.dto.CreateSalesPointRequest;
import com.supermarket.salespoint.application.dto.SalesPointResponse;
import com.supermarket.salespoint.application.dto.SalesPointStatusRequest;
import com.supermarket.salespoint.application.dto.UpdateSalesPointRequest;
import com.supermarket.salespoint.application.mapper.SalesPointMapper;
import com.supermarket.salespoint.domain.model.Address;
import com.supermarket.salespoint.domain.model.OpeningHours;
import com.supermarket.salespoint.domain.model.SalesPoint;
import com.supermarket.salespoint.domain.service.SalesPointDomainService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sales-points")
public class SalesPointController {

    private final SalesPointDomainService domainService;
    private final SalesPointMapper mapper;

    public SalesPointController(SalesPointDomainService domainService, SalesPointMapper mapper) {
        this.domainService = domainService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<Page<SalesPointResponse>> listAll(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<SalesPointResponse> page = domainService.listAll(pageable)
                .map(mapper::toResponse);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPointResponse> getById(@PathVariable UUID id) {
        SalesPoint salesPoint = domainService.getSalesPoint(id);
        return ResponseEntity.ok(mapper.toResponse(salesPoint));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<SalesPointResponse>> getByCity(@PathVariable String city) {
        List<SalesPointResponse> responses = domainService.findByCity(city)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<SalesPointResponse> create(@Valid @RequestBody CreateSalesPointRequest request) {
        Address address = new Address(
                request.street(),
                request.city(),
                request.postalCode(),
                request.province()
        );
        OpeningHours openingHours = new OpeningHours(request.opensAt(), request.closesAt());
        SalesPoint salesPoint = domainService.createSalesPoint(
                request.name(), address, request.phoneNumber(), openingHours);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salesPoint));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesPointResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateSalesPointRequest request) {
        Address address = new Address(
                request.street(),
                request.city(),
                request.postalCode(),
                request.province()
        );
        OpeningHours openingHours = new OpeningHours(request.opensAt(), request.closesAt());
        SalesPoint salesPoint = domainService.updateSalesPoint(
                id, request.name(), address, request.phoneNumber(), openingHours);
        return ResponseEntity.ok(mapper.toResponse(salesPoint));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SalesPointResponse> changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody SalesPointStatusRequest request) {
        SalesPoint salesPoint = domainService.changeStatus(id, request.status());
        return ResponseEntity.ok(mapper.toResponse(salesPoint));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        domainService.deleteSalesPoint(id);
        return ResponseEntity.noContent().build();
    }
}
