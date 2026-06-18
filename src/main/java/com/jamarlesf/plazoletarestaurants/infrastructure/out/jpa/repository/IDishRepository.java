package com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository;

import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {
}
