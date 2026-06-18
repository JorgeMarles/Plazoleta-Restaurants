package com.jamarlesf.plazoletarestaurants.application.mapper;

import com.jamarlesf.plazoletarestaurants.application.dto.response.DishResponseDto;
import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {ICategoryResponseMapper.class, IRestaurantResponseMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IDishResponseMapper {
    DishResponseDto toResponse(Dish dish);
    List<DishResponseDto> toResponseList(List<Dish> dishes);
}
