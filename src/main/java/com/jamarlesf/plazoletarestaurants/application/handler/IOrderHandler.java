package com.jamarlesf.plazoletarestaurants.application.handler;

import com.jamarlesf.plazoletarestaurants.application.dto.request.OrderRequestDto;

import com.jamarlesf.plazoletarestaurants.application.dto.response.OrderResponseDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.PageResponseDto;

public interface IOrderHandler {
    void saveOrder(OrderRequestDto orderRequestDto, Long customerId);
    PageResponseDto<OrderResponseDto> getOrdersByRestaurantAndStatus(Long restaurantId, String status, Integer page, Integer size);
}
