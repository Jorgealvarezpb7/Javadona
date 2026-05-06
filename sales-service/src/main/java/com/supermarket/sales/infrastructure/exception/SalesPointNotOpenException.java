package com.supermarket.sales.infrastructure.exception;

import java.util.UUID;

public class SalesPointNotOpenException extends RuntimeException {

    public SalesPointNotOpenException(UUID id) {
        super("Sales point is not open: " + id);
    }
}
