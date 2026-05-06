package com.supermarket.customer.domain.service;

import com.supermarket.customer.application.dto.CreateCustomerRequest;
import com.supermarket.customer.application.dto.UpdateCustomerRequest;
import com.supermarket.customer.domain.model.Address;
import com.supermarket.customer.domain.model.Customer;
import com.supermarket.customer.domain.model.DocumentId;
import com.supermarket.customer.domain.model.DocumentType;
import com.supermarket.customer.domain.model.Email;
import com.supermarket.customer.domain.model.PersonalDetails;
import com.supermarket.customer.domain.repository.CustomerRepository;
import com.supermarket.customer.infrastructure.exception.CustomerNotFoundException;
import com.supermarket.customer.infrastructure.exception.DuplicateDocumentException;
import com.supermarket.customer.infrastructure.exception.DuplicateEmailException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CustomerDomainService {

    private final CustomerRepository customerRepository;

    public CustomerDomainService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer registerCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already in use: " + request.getEmail());
        }
        if (customerRepository.existsByDocument(request.getDocumentType(), request.getDocumentValue())) {
            throw new DuplicateDocumentException("Document already registered: "
                    + request.getDocumentType() + " " + request.getDocumentValue());
        }

        DocumentId documentId = new DocumentId(request.getDocumentType(), request.getDocumentValue());
        PersonalDetails personalDetails = new PersonalDetails(
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                request.getPhoneNumber()
        );
        Email email = new Email(request.getEmail());
        Address address = new Address(
                request.getStreet(),
                request.getCity(),
                request.getPostalCode(),
                request.getProvince()
        );

        Customer customer = Customer.create(documentId, personalDetails, email, address);
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(UUID id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        PersonalDetails personalDetails = null;
        if (request.getFirstName() != null || request.getLastName() != null
                || request.getDateOfBirth() != null || request.getPhoneNumber() != null) {
            String firstName = request.getFirstName() != null
                    ? request.getFirstName() : customer.getPersonalDetails().firstName();
            String lastName = request.getLastName() != null
                    ? request.getLastName() : customer.getPersonalDetails().lastName();
            java.time.LocalDate dob = request.getDateOfBirth() != null
                    ? request.getDateOfBirth() : customer.getPersonalDetails().dateOfBirth();
            String phone = request.getPhoneNumber() != null
                    ? request.getPhoneNumber() : customer.getPersonalDetails().phoneNumber();
            personalDetails = new PersonalDetails(firstName, lastName, dob, phone);
        }

        Address address = null;
        if (request.getStreet() != null || request.getCity() != null
                || request.getPostalCode() != null || request.getProvince() != null) {
            String street = request.getStreet() != null
                    ? request.getStreet() : customer.getAddress().street();
            String city = request.getCity() != null
                    ? request.getCity() : customer.getAddress().city();
            String postalCode = request.getPostalCode() != null
                    ? request.getPostalCode() : customer.getAddress().postalCode();
            String province = request.getProvince() != null
                    ? request.getProvince() : customer.getAddress().province();
            address = new Address(street, city, postalCode, province);
        }

        customer.update(personalDetails, address);
        return customerRepository.save(customer);
    }

    public void softDeleteCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customer.softDelete();
        customerRepository.save(customer);
    }

    public Customer addRewardPoints(UUID id, int points) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customer.addRewardPoints(points);
        return customerRepository.save(customer);
    }

    public Customer addFrequentSalesPoint(UUID customerId, UUID salesPointId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        customer.addFrequentSalesPoint(salesPointId);
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer findById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Customer findByDocument(DocumentType type, String value) {
        return customerRepository.findByDocument(type, value)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with document: " + type + " " + value));
    }

    @Transactional(readOnly = true)
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
}
