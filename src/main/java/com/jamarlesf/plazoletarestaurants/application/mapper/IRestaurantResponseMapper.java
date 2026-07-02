package com.jamarlesf.plazoletarestaurants.application.mapper;

import com.jamarlesf.plazoletarestaurants.application.dto.response.PageResponseDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.RestaurantBasicResponseDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.RestaurantResponseDto;
import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;
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

    RestaurantBasicResponseDto toBasicResponse(Restaurant restaurant);

    List<RestaurantBasicResponseDto> toBasicResponseList(List<Restaurant> restaurants);

    default PageResponseDto<RestaurantBasicResponseDto> toPageResponseDto(PageModel<Restaurant> pageModel) {
        if (pageModel == null) {
            return null;
        }

        return new PageResponseDto<>(
                toBasicResponseList(pageModel.getContent()),
                pageModel.getPageNumber(),
                pageModel.getPageSize(),
                pageModel.getTotalElements(),
                pageModel.getTotalPages(),
                pageModel.isFirst(),
                pageModel.isLast()
        );
    }
}
