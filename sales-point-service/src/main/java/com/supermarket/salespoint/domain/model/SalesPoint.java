package com.supermarket.salespoint.domain.model;

import java.util.UUID;

public class SalesPoint {

    private UUID id;
    private String name;
    private Address address;
    private String phoneNumber;
    private OpeningHours openingHours;
    private SalesPointStatus status;

    public static SalesPoint create(String name, Address address, String phoneNumber, OpeningHours openingHours) {
        SalesPoint sp = new SalesPoint();
        sp.id = UUID.randomUUID();
        sp.name = name;
        sp.address = address;
        sp.phoneNumber = phoneNumber;
        sp.openingHours = openingHours;
        sp.status = SalesPointStatus.OPEN;
        return sp;
    }

    public void update(String name, Address address, String phoneNumber, OpeningHours openingHours) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openingHours = openingHours;
    }

    public void changeStatus(SalesPointStatus status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public SalesPointStatus getStatus() {
        return status;
    }

    public void setStatus(SalesPointStatus status) {
        this.status = status;
    }
}
