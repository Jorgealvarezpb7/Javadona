package com.supermarket.sales.infrastructure.client.dto;

import java.util.UUID;

public record SalesPointClientDto(
        UUID id,
        String status,
        String name
) {
}
