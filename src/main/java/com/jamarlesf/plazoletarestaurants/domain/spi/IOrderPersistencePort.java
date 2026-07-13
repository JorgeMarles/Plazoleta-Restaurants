package com.jamarlesf.plazoletarestaurants.domain.spi;

import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderStatus;
import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    void save(Order order);
    boolean hasActiveOrders(Long customerId, List<OrderStatus> statuses);
    PageModel<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, PaginationCriteria paginationCriteria);
    Optional<Order> findById(Long orderId);
}
