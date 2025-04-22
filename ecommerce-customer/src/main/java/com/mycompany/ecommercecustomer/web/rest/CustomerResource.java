package com.mycompany.ecommercecustomer.web.rest;

import com.mycompany.ecommercecustomer.domain.Customer;
import com.mycompany.ecommercecustomer.domain.Customer.Gender;
import com.mycompany.ecommercecustomer.repository.CustomerRepository;
import com.mycompany.ecommercecustomer.web.rest.errors.BadRequestAlertException;
import com.mycompany.ecommercecustomer.web.rest.errors.InternalServerErrorAlertException;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@Transactional
public class CustomerResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerResource.class);
    private static final String ENTITY_NAME = "ecommerceCustomerCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerRepository customerRepository;

    public CustomerResource(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping("")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) throws URISyntaxException {
        LOG.debug("REST request to save Customer : {}", customer);

        if (customer.getId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        }

        validateCustomerFields(customer);

        try {
            customer = customerRepository.save(customer);
        } catch (DataAccessException e) {
            throw new InternalServerErrorAlertException("Error saving customer", ENTITY_NAME, e.getMessage());
        }

        return ResponseEntity.created(new URI("/api/customers/" + customer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, customer.getId().toString()))
            .body(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
        @PathVariable(value = "id") final Long id,
        @RequestBody Customer customer
    ) throws URISyntaxException {
        LOG.debug("REST request to update Customer : {}, {}", id, customer);

        if (customer.getId() == null || !Objects.equals(id, customer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        validateCustomerFields(customer);
        customer = customerRepository.save(customer);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customer.getId().toString()))
            .body(customer);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Customer> partialUpdateCustomer(
        @PathVariable(value = "id") final Long id,
        @RequestBody Customer customer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Customer : {}, {}", id, customer);

        if (customer.getId() == null || !Objects.equals(id, customer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Customer> result = customerRepository
            .findById(customer.getId())
            .map(existingCustomer -> {
                if (customer.getFirstName() != null) {
                    existingCustomer.setFirstName(customer.getFirstName());
                }
                if (customer.getLastName() != null) {
                    existingCustomer.setLastName(customer.getLastName());
                }
                if (customer.getPhoneNumber() != null) {
                    existingCustomer.setPhoneNumber(customer.getPhoneNumber());
                }
                if (customer.getDateOfBirth() != null) {
                    existingCustomer.setDateOfBirth(customer.getDateOfBirth());
                }
                if (customer.getGender() != null) {
                    existingCustomer.setGender(customer.getGender());
                }
                if (customer.getStreet() != null) {
                    existingCustomer.setStreet(customer.getStreet());
                }
                if (customer.getPostalCode() != null) {
                    existingCustomer.setPostalCode(customer.getPostalCode());
                }
                if (customer.getMembership() != null) {
                    existingCustomer.setMembership(customer.getMembership());
                }
                if (customer.getUserId() != null) {
                    existingCustomer.setUserId(customer.getUserId());
                }
                return existingCustomer;
            })
            .map(customerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customer.getId().toString())
        );
    }

    @GetMapping("")
    public List<Customer> getAllCustomers() {
        LOG.debug("REST request to get all Customers");
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Customer : {}", id);
        Optional<Customer> customer = customerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Customer : {}", id);
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        customerRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    private void validateCustomerFields(Customer customer) {
        if (isNullOrEmpty(customer.getFirstName())) {
            throw new BadRequestAlertException("First name cannot be null or empty", ENTITY_NAME, "firstnameinvalid");
        }

        if (isNullOrEmpty(customer.getLastName())) {
            throw new BadRequestAlertException("Last name cannot be null or empty", ENTITY_NAME, "lastnameinvalid");
        }

        if (isNullOrEmpty(customer.getPhoneNumber())) {
            throw new BadRequestAlertException("Phone number cannot be null or empty", ENTITY_NAME, "phonenumberinvalid");
        }

        if (!customer.getPhoneNumber().matches("^(\\+65[\\s\\-]?)?[689]\\d{3}[\\s\\-]?\\d{4}$")) {
            throw new BadRequestAlertException("Invalid phone number format", ENTITY_NAME, "invalidphonenumber");
        }

        if (customer.getDateOfBirth() == null) {
            throw new BadRequestAlertException("Date of birth cannot be null", ENTITY_NAME, "dateofbirthinvalid");
        }

        if (customer.getGender() == null || !(customer.getGender() instanceof Gender)) {
            throw new BadRequestAlertException("Invalid or missing gender", ENTITY_NAME, "genderinvalid");
        }

        if (isNullOrEmpty(customer.getStreet())) {
            throw new BadRequestAlertException("Street cannot be null or empty", ENTITY_NAME, "streetinvalid");
        }

        if (isNullOrEmpty(customer.getPostalCode())) {
            throw new BadRequestAlertException("Postal code cannot be null or empty", ENTITY_NAME, "postalcodinvalid");
        }

        if (customer.getMembership() == null) {
            throw new BadRequestAlertException("Membership cannot be null", ENTITY_NAME, "membershipinvalid");
        }

        if (customer.getUserId() == null) {
            throw new BadRequestAlertException("User ID cannot be null", ENTITY_NAME, "useridinvalid");
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
