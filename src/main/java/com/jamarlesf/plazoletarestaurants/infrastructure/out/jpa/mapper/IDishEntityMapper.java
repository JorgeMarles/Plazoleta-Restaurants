package com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper;

import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {ICategoryEntityMapper.class, IRestaurantEntityMapper.class},
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface IDishEntityMapper {
    DishEntity toEntity(Dish dish);
    Dish toDish(DishEntity dishEntity);
    List<Dish> toDishList(List<DishEntity> dishEntityList);
}
