package com.mycompany.order2.repository;

import com.mycompany.order2.domain.Order2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Order2 entity.
 */
@Repository
public interface Order2Repository extends MongoRepository<Order2, String> {}
