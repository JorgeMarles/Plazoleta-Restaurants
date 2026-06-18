package com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper;

import com.jamarlesf.plazoletarestaurants.domain.model.Category;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ICategoryEntityMapper {
    CategoryEntity toEntity(Category category);
    Category toCategory(CategoryEntity categoryEntity);
}
