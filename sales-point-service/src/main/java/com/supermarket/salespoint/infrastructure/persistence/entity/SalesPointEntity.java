package com.supermarket.salespoint.infrastructure.persistence.entity;

import com.supermarket.salespoint.domain.model.SalesPointStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "sales_points")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesPointEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    private String street;
    private String city;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    private String province;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "opens_at", nullable = false)
    private LocalTime opensAt;

    @Column(name = "closes_at", nullable = false)
    private LocalTime closesAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private SalesPointStatus status;
}
