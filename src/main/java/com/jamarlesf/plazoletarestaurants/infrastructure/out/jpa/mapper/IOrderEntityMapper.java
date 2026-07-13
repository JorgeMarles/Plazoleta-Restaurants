package com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper;

import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderDish;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.entity.OrderDishEntity;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {
    Order toOrder(OrderEntity orderEntity);
    OrderEntity toOrderEntity(Order order);
    
    @Mapping(target = "id.dishId", source = "dishId")
    OrderDishEntity toOrderDishEntity(OrderDish orderDish);
    
    @Mapping(target = "dishId", source = "id.dishId")
    OrderDish toOrderDish(OrderDishEntity orderDishEntity);

    @Mapping(target = "dishes", ignore = true)
    void updateOrderEntityFromOrder(Order order, @org.mapstruct.MappingTarget OrderEntity orderEntity);
}
