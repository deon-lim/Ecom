package com.order.service.impl;

import com.order.domain.Order;
import com.order.repository.OrderRepository;
import com.order.service.OrderService;
import com.order.service.dto.OrderDTO;
import com.order.service.mapper.OrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.order.domain.Order}.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        LOG.debug("Request to save Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDTO update(OrderDTO orderDTO) {
        LOG.debug("Request to update Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public Optional<OrderDTO> partialUpdate(OrderDTO orderDTO) {
        LOG.debug("Request to partially update Order : {}", orderDTO);

        return orderRepository
            .findById(orderDTO.getId())
            .map(existingOrder -> {
                orderMapper.partialUpdate(existingOrder, orderDTO);

                return existingOrder;
            })
            .map(orderRepository::save)
            .map(orderMapper::toDto);
    }

    @Override
    public Page<OrderDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Orders");
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Override
    public Optional<OrderDTO> findOne(String id) {
        LOG.debug("Request to get Order : {}", id);
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }
}
