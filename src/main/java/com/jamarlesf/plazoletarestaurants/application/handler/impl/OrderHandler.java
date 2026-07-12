package com.jamarlesf.plazoletarestaurants.application.handler.impl;

import com.jamarlesf.plazoletarestaurants.application.dto.request.OrderRequestDto;
import com.jamarlesf.plazoletarestaurants.application.handler.IOrderHandler;
import com.jamarlesf.plazoletarestaurants.application.mapper.IOrderRequestMapper;
import com.jamarlesf.plazoletarestaurants.domain.api.IOrderServicePort;
import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;

    @Override
    public void saveOrder(OrderRequestDto orderRequestDto, Long customerId) {
        Order order = orderRequestMapper.toOrder(orderRequestDto);
        order.setCustomerId(customerId);
        orderServicePort.save(order);
    }
}
