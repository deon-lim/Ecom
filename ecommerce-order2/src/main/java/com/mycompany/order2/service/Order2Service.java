package com.mycompany.order2.service;

import com.mycompany.order2.domain.Order2;
import com.mycompany.order2.repository.Order2Repository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.mycompany.order2.domain.Order2}.
 */
@Service
public class Order2Service {

    private static final Logger LOG = LoggerFactory.getLogger(Order2Service.class);

    private final Order2Repository order2Repository;

    public Order2Service(Order2Repository order2Repository) {
        this.order2Repository = order2Repository;
    }

    /**
     * Save a order2.
     *
     * @param order2 the entity to save.
     * @return the persisted entity.
     */
    public Order2 save(Order2 order2) {
        LOG.debug("Request to save Order2 : {}", order2);
        return order2Repository.save(order2);
    }

    /**
     * Update a order2.
     *
     * @param order2 the entity to save.
     * @return the persisted entity.
     */
    public Order2 update(Order2 order2) {
        LOG.debug("Request to update Order2 : {}", order2);
        return order2Repository.save(order2);
    }

    /**
     * Partially update a order2.
     *
     * @param order2 the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Order2> partialUpdate(Order2 order2) {
        LOG.debug("Request to partially update Order2 : {}", order2);

        return order2Repository
            .findById(order2.getId())
            .map(existingOrder2 -> {
                if (order2.getCustomerId() != null) {
                    existingOrder2.setCustomerId(order2.getCustomerId());
                }
                if (order2.getOrderStatus() != null) {
                    existingOrder2.setOrderStatus(order2.getOrderStatus());
                }
                if (order2.getTotalAmount() != null) {
                    existingOrder2.setTotalAmount(order2.getTotalAmount());
                }
                if (order2.getCreatedOn() != null) {
                    existingOrder2.setCreatedOn(order2.getCreatedOn());
                }

                return existingOrder2;
            })
            .map(order2Repository::save);
    }

    /**
     * Get all the order2s.
     *
     * @return the list of entities.
     */
    public List<Order2> findAll() {
        LOG.debug("Request to get all Order2s");
        return order2Repository.findAll();
    }

    /**
     * Get one order2 by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Order2> findOne(String id) {
        LOG.debug("Request to get Order2 : {}", id);
        return order2Repository.findById(id);
    }

    /**
     * Delete the order2 by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Order2 : {}", id);
        order2Repository.deleteById(id);
    }
}
