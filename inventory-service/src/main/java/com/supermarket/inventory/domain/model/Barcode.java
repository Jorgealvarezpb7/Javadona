package com.supermarket.inventory.domain.model;

public record Barcode(String value) {

    public Barcode {
        if (value == null || !value.matches("\\d{13}")) {
            throw new IllegalArgumentException("Barcode must be a 13-digit EAN-13 value");
        }
    }
}
