package com.jamarlesf.plazoletarestaurants.domain.spi;

import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderStatus;

import java.util.List;

public interface IOrderPersistencePort {
    void save(Order order);
    boolean hasActiveOrders(Long customerId, List<OrderStatus> statuses);
}
