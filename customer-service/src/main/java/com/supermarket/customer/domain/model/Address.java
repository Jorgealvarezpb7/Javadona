package com.supermarket.customer.domain.model;

public record Address(
    String street,
    String city,
    String postalCode,
    String province
) {}
