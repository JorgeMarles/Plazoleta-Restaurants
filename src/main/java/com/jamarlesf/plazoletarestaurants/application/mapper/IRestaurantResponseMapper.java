package com.jamarlesf.plazoletarestaurants.application.mapper;

import com.jamarlesf.plazoletarestaurants.application.dto.response.RestaurantResponseDto;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IRestaurantResponseMapper {
    RestaurantResponseDto toResponse(Restaurant restaurant);

    List<RestaurantResponseDto> toResponseList(List<Restaurant> restaurants);
}
