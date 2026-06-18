package com.jamarlesf.plazoletarestaurants.application.mapper;

import com.jamarlesf.plazoletarestaurants.application.dto.response.CategoryResponseDto;
import com.jamarlesf.plazoletarestaurants.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ICategoryResponseMapper {
    CategoryResponseDto toResponse(Category category);
}
