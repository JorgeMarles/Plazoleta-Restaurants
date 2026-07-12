package com.jamarlesf.plazoletarestaurants.application.handler;

import com.jamarlesf.plazoletarestaurants.application.dto.request.OrderRequestDto;

public interface IOrderHandler {
    void saveOrder(OrderRequestDto orderRequestDto, Long customerId);
}
