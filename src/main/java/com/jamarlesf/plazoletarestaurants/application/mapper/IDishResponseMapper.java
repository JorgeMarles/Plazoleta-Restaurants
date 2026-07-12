package com.jamarlesf.plazoletarestaurants.application.mapper;

import com.jamarlesf.plazoletarestaurants.application.dto.response.DishResponseDto;
import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import com.jamarlesf.plazoletarestaurants.application.dto.response.PageResponseDto;
import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;

@Mapper(
        componentModel = "spring",
        uses = {ICategoryResponseMapper.class, IRestaurantResponseMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IDishResponseMapper {
    @org.mapstruct.Mapping(source = "restaurant.id", target = "restaurantId")
    DishResponseDto toResponse(Dish dish);
    List<DishResponseDto> toResponseList(List<Dish> dishes);

    default PageResponseDto<DishResponseDto> toPageResponseDto(PageModel<Dish> pageModel) {
        if (pageModel == null) {
            return null;
        }

        return new PageResponseDto<>(
                toResponseList(pageModel.getContent()),
                pageModel.getPageNumber(),
                pageModel.getPageSize(),
                pageModel.getTotalElements(),
                pageModel.getTotalPages(),
                pageModel.isFirst(),
                pageModel.isLast()
        );
    }
}
