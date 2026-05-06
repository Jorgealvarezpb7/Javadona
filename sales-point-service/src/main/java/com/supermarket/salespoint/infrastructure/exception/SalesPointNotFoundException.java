package com.supermarket.salespoint.infrastructure.exception;

import java.util.UUID;

public class SalesPointNotFoundException extends RuntimeException {

    public SalesPointNotFoundException(UUID id) {
        super("Sales point not found with id: " + id);
    }
}
