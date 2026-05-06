package com.supermarket.sales.infrastructure.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException() {
        super("Insufficient stock for one or more products");
    }
}
