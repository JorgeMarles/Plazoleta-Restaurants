package com.jamarlesf.plazoletarestaurants.application.handler;

import com.jamarlesf.plazoletarestaurants.application.dto.request.OrderRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.OrderResponseDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.PageResponseDto;

public interface IOrderHandler {
    void saveOrder(OrderRequestDto orderRequestDto, Long customerId, String userEmail);
    PageResponseDto<OrderResponseDto> getOrdersByRestaurantAndStatus(Long restaurantId, String status, Integer page, Integer size);
    void assignOrder(Long orderId, Long employeeId, String employeeEmail);
    void markAsReady(Long orderId);
    void markAsDelivered(Long orderId, String pin);
    void cancelOrder(Long orderId, Long customerId);
}
