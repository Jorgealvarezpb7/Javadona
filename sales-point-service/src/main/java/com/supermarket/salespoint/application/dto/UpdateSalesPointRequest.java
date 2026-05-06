package com.supermarket.salespoint.application.dto;

import java.time.LocalTime;

public record UpdateSalesPointRequest(
        String name,
        String street,
        String city,
        String postalCode,
        String province,
        String phoneNumber,
        LocalTime opensAt,
        LocalTime closesAt
) {
}
