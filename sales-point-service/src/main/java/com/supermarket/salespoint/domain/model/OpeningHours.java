package com.supermarket.salespoint.domain.model;

import java.time.LocalTime;

public record OpeningHours(
        LocalTime opensAt,
        LocalTime closesAt
) {
}
