package com.jamarlesf.plazoletarestaurants.application.mapper;

import com.jamarlesf.plazoletarestaurants.application.dto.request.OrderDishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.request.OrderRequestDto;
import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderRequestMapper {
    Order toOrder(OrderRequestDto orderRequestDto);
    OrderDish toOrderDish(OrderDishRequestDto orderDishRequestDto);
}
