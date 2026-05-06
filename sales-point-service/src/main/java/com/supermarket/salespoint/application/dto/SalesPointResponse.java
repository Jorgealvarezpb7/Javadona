package com.supermarket.salespoint.application.dto;

import java.util.UUID;

public record SalesPointResponse(
        UUID id,
        String name,
        String street,
        String city,
        String postalCode,
        String province,
        String phoneNumber,
        String opensAt,
        String closesAt,
        String status
) {
}
