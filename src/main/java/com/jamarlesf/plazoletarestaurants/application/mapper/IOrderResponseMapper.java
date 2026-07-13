package com.jamarlesf.plazoletarestaurants.application.mapper;

import com.jamarlesf.plazoletarestaurants.application.dto.response.OrderResponseDto;
import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderResponseMapper {
    OrderResponseDto toOrderResponseDto(Order order);
    List<OrderResponseDto> toOrderResponseDtoList(List<Order> orderList);
}
