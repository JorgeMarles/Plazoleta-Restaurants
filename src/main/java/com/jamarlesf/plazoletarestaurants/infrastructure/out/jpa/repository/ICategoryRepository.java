package com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository;

import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
