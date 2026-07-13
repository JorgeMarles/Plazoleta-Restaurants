package com.jamarlesf.plazoletarestaurants.application.handler.impl;

import com.jamarlesf.plazoletarestaurants.application.dto.request.OrderRequestDto;
import com.jamarlesf.plazoletarestaurants.application.handler.IOrderHandler;
import com.jamarlesf.plazoletarestaurants.application.mapper.IOrderRequestMapper;
import com.jamarlesf.plazoletarestaurants.domain.api.IOrderServicePort;
import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.application.dto.response.OrderDishResponseDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.OrderResponseDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.PageResponseDto;
import com.jamarlesf.plazoletarestaurants.application.mapper.IDishResponseMapper;
import com.jamarlesf.plazoletarestaurants.application.mapper.IOrderResponseMapper;
import com.jamarlesf.plazoletarestaurants.domain.api.IDishServicePort;
import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderDish;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderStatus;
import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IDishServicePort dishServicePort;
    private final IOrderResponseMapper orderResponseMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public void saveOrder(OrderRequestDto orderRequestDto, Long customerId) {
        Order order = orderRequestMapper.toOrder(orderRequestDto);
        order.setCustomerId(customerId);
        orderServicePort.save(order);
    }

    @Override
    public PageResponseDto<OrderResponseDto> getOrdersByRestaurantAndStatus(Long restaurantId, String status, Integer page, Integer size) {
        PaginationCriteria criteria = new PaginationCriteria(page, size);
        PageModel<Order> orderPage = orderServicePort.findByRestaurantIdAndStatus(restaurantId, OrderStatus.valueOf(status.toUpperCase()), criteria);
        
        List<OrderResponseDto> orderResponseDtos = orderResponseMapper.toOrderResponseDtoList(orderPage.getContent());
        
        for (int i = 0; i < orderPage.getContent().size(); i++) {
            Order order = orderPage.getContent().get(i);
            OrderResponseDto dto = orderResponseDtos.get(i);
            
            for (int j = 0; j < order.getDishes().size(); j++) {
                OrderDish orderDish = order.getDishes().get(j);
                OrderDishResponseDto dishDto = dto.getDishes().get(j);
                
                Dish dish = dishServicePort.getDish(orderDish.getDishId());
                dishDto.setDish(dishResponseMapper.toResponse(dish));
            }
        }
        
        return new PageResponseDto<>(
                orderResponseDtos,
                orderPage.getPageNumber(),
                orderPage.getPageSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isFirst(),
                orderPage.isLast()
        );
    }

    @Override
    public void assignOrder(Long orderId, Long employeeId) {
        orderServicePort.assignOrder(orderId, employeeId);
    }
}
