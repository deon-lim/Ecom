package com.mycompany.order2.web.rest;

import com.mycompany.order2.domain.Order2;
import com.mycompany.order2.repository.Order2Repository;
import com.mycompany.order2.service.Order2Service;
import com.mycompany.order2.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.order2.domain.Order2}.
 */
@RestController
@RequestMapping("/api/order-2-s")
public class Order2Resource {

    private static final Logger LOG = LoggerFactory.getLogger(Order2Resource.class);

    private static final String ENTITY_NAME = "ecommerceOrder2Order2";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Order2Service order2Service;

    private final Order2Repository order2Repository;

    public Order2Resource(Order2Service order2Service, Order2Repository order2Repository) {
        this.order2Service = order2Service;
        this.order2Repository = order2Repository;
    }

    /**
     * {@code POST  /order-2-s} : Create a new order2.
     *
     * @param order2 the order2 to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new order2, or with status {@code 400 (Bad Request)} if the order2 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Order2> createOrder2(@RequestBody Order2 order2) throws URISyntaxException {
        LOG.debug("REST request to save Order2 : {}", order2);
        if (order2.getId() != null) {
            throw new BadRequestAlertException("A new order2 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        order2 = order2Service.save(order2);
        return ResponseEntity.created(new URI("/api/order-2-s/" + order2.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, order2.getId()))
            .body(order2);
    }

    /**
     * {@code PUT  /order-2-s/:id} : Updates an existing order2.
     *
     * @param id the id of the order2 to save.
     * @param order2 the order2 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated order2,
     * or with status {@code 400 (Bad Request)} if the order2 is not valid,
     * or with status {@code 500 (Internal Server Error)} if the order2 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order2> updateOrder2(@PathVariable(value = "id", required = false) final String id, @RequestBody Order2 order2)
        throws URISyntaxException {
        LOG.debug("REST request to update Order2 : {}, {}", id, order2);
        if (order2.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, order2.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!order2Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        order2 = order2Service.update(order2);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, order2.getId()))
            .body(order2);
    }

    /**
     * {@code PATCH  /order-2-s/:id} : Partial updates given fields of an existing order2, field will ignore if it is null
     *
     * @param id the id of the order2 to save.
     * @param order2 the order2 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated order2,
     * or with status {@code 400 (Bad Request)} if the order2 is not valid,
     * or with status {@code 404 (Not Found)} if the order2 is not found,
     * or with status {@code 500 (Internal Server Error)} if the order2 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Order2> partialUpdateOrder2(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Order2 order2
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Order2 partially : {}, {}", id, order2);
        if (order2.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, order2.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!order2Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Order2> result = order2Service.partialUpdate(order2);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, order2.getId()));
    }

    /**
     * {@code GET  /order-2-s} : get all the order2s.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of order2s in body.
     */
    @GetMapping("")
    public List<Order2> getAllOrder2s() {
        LOG.debug("REST request to get all Order2s");
        return order2Service.findAll();
    }

    /**
     * {@code GET  /order-2-s/:id} : get the "id" order2.
     *
     * @param id the id of the order2 to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the order2, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order2> getOrder2(@PathVariable("id") String id) {
        LOG.debug("REST request to get Order2 : {}", id);
        Optional<Order2> order2 = order2Service.findOne(id);
        return ResponseUtil.wrapOrNotFound(order2);
    }

    /**
     * {@code DELETE  /order-2-s/:id} : delete the "id" order2.
     *
     * @param id the id of the order2 to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder2(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Order2 : {}", id);
        order2Service.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
