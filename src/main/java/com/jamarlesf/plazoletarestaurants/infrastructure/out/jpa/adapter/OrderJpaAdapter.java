package com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.adapter;

import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderStatus;
import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;
import com.jamarlesf.plazoletarestaurants.domain.spi.IOrderPersistencePort;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.entity.OrderEntity;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository.IOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public void save(Order order) {
        if (order.getId() != null) {
            OrderEntity existingEntity = orderRepository.findById(order.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada con id " + order.getId()));
            
            orderEntityMapper.updateOrderEntityFromOrder(order, existingEntity);
            orderRepository.save(existingEntity);
        } else {
            OrderEntity newEntity = orderEntityMapper.toOrderEntity(order);
            if (newEntity.getDishes() != null) {
                newEntity.getDishes().forEach(dish -> dish.setOrder(newEntity));
            }
            OrderEntity savedEntity = orderRepository.save(newEntity);
            order.setId(savedEntity.getId());
        }
    }

    @Override
    public boolean hasActiveOrders(Long customerId, List<OrderStatus> statuses) {
        return orderRepository.existsByCustomerIdAndStatusIn(customerId, statuses);
    }

    @Override
    public PageModel<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, PaginationCriteria paginationCriteria) {
        Pageable pageable = PageRequest.of(paginationCriteria.getPageNumber(), paginationCriteria.getPageSize());
        
        Page<OrderEntity> orderEntityPage = orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);
        
        List<Order> orders = orderEntityPage.getContent().stream()
                .map(orderEntityMapper::toOrder)
                .collect(Collectors.toList());
                
        return new PageModel<>(
                orders,
                orderEntityPage.getNumber(),
                orderEntityPage.getSize(),
                orderEntityPage.getTotalElements(),
                orderEntityPage.getTotalPages(),
                orderEntityPage.isFirst(),
                orderEntityPage.isLast()
        );
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderEntityMapper::toOrder);
    }
}
