package com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.adapter;

import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderStatus;
import com.jamarlesf.plazoletarestaurants.domain.spi.IOrderPersistencePort;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.entity.OrderEntity;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public void save(Order order) {
        OrderEntity orderEntity = orderEntityMapper.toOrderEntity(order);
        if (orderEntity.getDishes() != null) {
            orderEntity.getDishes().forEach(dish -> dish.setOrder(orderEntity));
        }
        orderRepository.save(orderEntity);
    }

    @Override
    public boolean hasActiveOrders(Long customerId, List<OrderStatus> statuses) {
        return orderRepository.existsByCustomerIdAndStatusIn(customerId, statuses);
    }
}
