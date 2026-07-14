package com.jamarlesf.plazoletarestaurants.domain.api;

import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderStatus;
import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;

public interface IOrderServicePort {
    void save(Order order);
    PageModel<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, PaginationCriteria paginationCriteria);
    void assignOrder(Long orderId, Long employeeId);
    void markAsReady(Long orderId);
    void markAsDelivered(Long orderId, String pin);
}
