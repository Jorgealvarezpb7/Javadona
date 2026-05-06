package com.supermarket.customer.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Customer {

    private UUID id;
    private DocumentId documentId;
    private PersonalDetails personalDetails;
    private Email email;
    private Address address;
    private int rewardPoints;
    private Set<UUID> frequentSalesPoints;
    private CustomerStatus status;

    public static Customer create(DocumentId documentId, PersonalDetails personalDetails,
                                   Email email, Address address) {
        Customer c = new Customer();
        c.id = UUID.randomUUID();
        c.documentId = documentId;
        c.personalDetails = personalDetails;
        c.email = email;
        c.address = address;
        c.rewardPoints = 0;
        c.frequentSalesPoints = new HashSet<>();
        c.status = CustomerStatus.ACTIVE;
        return c;
    }

    public void addRewardPoints(int points) {
        this.rewardPoints += points;
    }

    public void addFrequentSalesPoint(UUID salesPointId) {
        this.frequentSalesPoints.add(salesPointId);
    }

    public void softDelete() {
        this.status = CustomerStatus.DELETED;
    }

    public void update(PersonalDetails personalDetails, Address address) {
        if (personalDetails != null) {
            this.personalDetails = personalDetails;
        }
        if (address != null) {
            this.address = address;
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public void setDocumentId(DocumentId documentId) {
        this.documentId = documentId;
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public Set<UUID> getFrequentSalesPoints() {
        return frequentSalesPoints;
    }

    public void setFrequentSalesPoints(Set<UUID> frequentSalesPoints) {
        this.frequentSalesPoints = frequentSalesPoints;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }
}
