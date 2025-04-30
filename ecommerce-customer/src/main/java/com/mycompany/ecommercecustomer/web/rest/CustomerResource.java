package com.mycompany.ecommercecustomer.web.rest;

import com.mycompany.ecommercecustomer.repository.CustomerRepository;
import com.mycompany.ecommercecustomer.service.CustomerService;
import com.mycompany.ecommercecustomer.service.dto.CustomerDTO;
import com.mycompany.ecommercecustomer.web.rest.errors.BadRequestAlertException;
import com.mycompany.ecommercecustomer.web.rest.errors.InternalServerErrorAlertException;
import com.mycompany.ecommercecustomer.domain.enumeration.MembershipStatus;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.dao.DataAccessException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;


/**
 * REST controller for managing {@link com.mycompany.ecommercecustomer.domain.Customer}.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "ecommerceCustomerCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    public CustomerResource(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    /**
     * {@code POST  /customers/create-by-user} : Create a new customer with minimal fields.
     *
     * @param payload a map containing userId and email.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)}.
     */
    @PostMapping("/create-by-user")
    public ResponseEntity<CustomerDTO> createCustomerFromUserAccount(@RequestBody Map<String, String> payload) throws URISyntaxException {
        LOG.debug("REST request to create Customer from User account : {}", payload);

        String userId = payload.get("userId");
        String email = payload.get("email");

        if (userId == null || email == null) {
            throw new BadRequestAlertException("User ID and Email are required", ENTITY_NAME, "useridemailrequired");
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setUserId(userId);
        customerDTO.setEmail(email);
        customerDTO.setLoyaltyPoints(0);
        customerDTO.setMembershipStatus(MembershipStatus.Bronze);


        try {
            customerDTO = customerService.save(customerDTO);
        } catch (DataAccessException e) {
            throw new InternalServerErrorAlertException("Error saving customer", ENTITY_NAME, e.getMessage());
        }

        return ResponseEntity.created(new URI("/api/customers/" + customerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, customerDTO.getId().toString()))
            .body(customerDTO);
    }

    /**
     * {@code POST  /customers} : Create a new customer.
     *
     * @param customerDTO the customerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerDTO, or with status {@code 400 (Bad Request)} if the customer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        LOG.debug("REST request to save Customer : {}", customerDTO);
        if (customerDTO.getId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        }

        validateCustomerFields(customerDTO);
        //If user does not already exits, save customerID, catch any error
        try {
            customerDTO = customerService.save(customerDTO);
        } catch (DataAccessException e) {
            throw new InternalServerErrorAlertException("Error saving customer", ENTITY_NAME, e.getMessage());
        }

        //customerDTO = customerService.save(customerDTO);
        return ResponseEntity.created(new URI("/api/customers/" + customerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, customerDTO.getId().toString()))
            .body(customerDTO);
    }

    /**
     * {@code PUT  /customers/:id} : Updates an existing customer.
     *
     * @param id the id of the customerDTO to save.
     * @param customerDTO the customerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDTO,
     * or with status {@code 400 (Bad Request)} if the customerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Customer : {}, {}", id, customerDTO);
        if (customerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        validateCustomerFields(customerDTO);
        customerDTO = customerService.update(customerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerDTO.getId().toString()))
            .body(customerDTO);
    }

    /**
     * {@code PATCH  /customers/:id} : Partial updates given fields of an existing customer, field will ignore if it is null
     *
     * @param id the id of the customerDTO to save.
     * @param customerDTO the customerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDTO,
     * or with status {@code 400 (Bad Request)} if the customerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerDTO> partialUpdateCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Customer partially : {}, {}", id, customerDTO);
        if (customerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerDTO> result = customerService.partialUpdate(customerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customers} : get all the customers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Customers");
        Page<CustomerDTO> page = customerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customers/:id} : get the "id" customer.
     *
     * @param id the id of the customerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Customer : {}", id);
        Optional<CustomerDTO> customerDTO = customerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerDTO);
    }

    /**
     * {@code DELETE  /customers/:id} : delete the "id" customer.
     *
     * @param id the id of the customerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Customer : {}", id);
        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idnotfound");
        }

        customerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    //Pre-checks
    private void validateCustomerFields(CustomerDTO customerDTO) {
//        if (isNullOrEmpty(customerDTO.getFirstName())) {
//            throw new BadRequestAlertException("First name cannot be null or empty", ENTITY_NAME, "firstnameinvalid");
//        }
//
//        if (isNullOrEmpty(customerDTO.getLastName())) {
//            throw new BadRequestAlertException("Last name cannot be null or empty", ENTITY_NAME, "lastnameinvalid");
//        }
//
//        if (isNullOrEmpty(customerDTO.getPhoneNumber())) {
//            throw new BadRequestAlertException("Phone number cannot be null or empty", ENTITY_NAME, "phonenumberinvalid");
//        }
//
//        if (!customerDTO.getPhoneNumber().matches("^(\\+65[\\s\\-]?)?[689]\\d{3}[\\s\\-]?\\d{4}$")) {
//            throw new BadRequestAlertException("Invalid phone number format", ENTITY_NAME, "invalidphonenumber");
//        }
//
//        if (customerDTO.getDateOfBirth() == null) {
//            throw new BadRequestAlertException("Date of birth cannot be null", ENTITY_NAME, "dateofbirthinvalid");
//        }
//
//        if (customerDTO.getGender() == null || !(customerDTO.getGender() instanceof gender)) {
//            throw new BadRequestAlertException("Invalid or missing gender", ENTITY_NAME, "genderinvalid");
//        }
//
//        if (isNullOrEmpty(customerDTO.getStreet())) {
//            throw new BadRequestAlertException("Street cannot be null or empty", ENTITY_NAME, "streetinvalid");
//        }
//
//        if (isNullOrEmpty(customerDTO.getPostalCode())) {
//            throw new BadRequestAlertException("Postal code cannot be null or empty", ENTITY_NAME, "postalcodinvalid");
//        }
//
//        if (customerDTO.getMembership() == null) {
//            throw new BadRequestAlertException("Membership cannot be null", ENTITY_NAME, "membershipinvalid");
//        }
//
//        if (customerDTO.getUserId() == null) {
//            throw new BadRequestAlertException("User ID cannot be null", ENTITY_NAME, "useridinvalid");
//        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
