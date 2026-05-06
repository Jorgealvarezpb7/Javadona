package com.supermarket.customer.domain.model;

import java.time.LocalDate;

public record PersonalDetails(
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String phoneNumber
) {}
