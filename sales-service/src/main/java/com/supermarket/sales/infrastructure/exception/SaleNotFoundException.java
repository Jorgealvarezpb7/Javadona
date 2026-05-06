package com.supermarket.sales.infrastructure.exception;

import java.util.UUID;

public class SaleNotFoundException extends RuntimeException {

    public SaleNotFoundException(UUID id) {
        super("Sale not found with id: " + id);
    }
}
