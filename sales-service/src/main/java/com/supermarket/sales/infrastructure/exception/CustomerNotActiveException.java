package com.supermarket.sales.infrastructure.exception;

import java.util.UUID;

public class CustomerNotActiveException extends RuntimeException {

    public CustomerNotActiveException(UUID id) {
        super("Customer is not active: " + id);
    }
}
