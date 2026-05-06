package com.supermarket.customer.infrastructure.persistence.entity;

import com.supermarket.customer.domain.model.CustomerStatus;
import com.supermarket.customer.domain.model.DocumentType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 3, nullable = false)
    private DocumentType documentType;

    @Column(name = "document_value", length = 20, nullable = false)
    private String documentValue;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    private String street;
    private String city;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    private String province;

    @Column(name = "reward_points", nullable = false)
    private int rewardPoints;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "customer_frequent_sales_points",
            joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "sales_point_id", columnDefinition = "BINARY(16)")
    @Builder.Default
    private Set<UUID> frequentSalesPoints = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private CustomerStatus status;
}
