package com.supermarket.sales.infrastructure.client.dto;

import java.util.UUID;

public record CustomerClientDto(
        UUID id,
        String status,
        String firstName,
        String lastName
) {
}
