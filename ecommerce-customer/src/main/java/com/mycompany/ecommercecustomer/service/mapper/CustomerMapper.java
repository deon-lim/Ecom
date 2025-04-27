package com.mycompany.ecommercecustomer.service.mapper;

import com.mycompany.ecommercecustomer.domain.Customer;
import com.mycompany.ecommercecustomer.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {}
