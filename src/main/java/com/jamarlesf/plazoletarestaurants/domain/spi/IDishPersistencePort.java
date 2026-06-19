package com.jamarlesf.plazoletarestaurants.domain.spi;

import com.jamarlesf.plazoletarestaurants.domain.model.Dish;

import java.util.List;
import java.util.Optional;

public interface IDishPersistencePort {
    void save(Dish dish);
    List<Dish> findAll();
    Optional<Dish> findById(Long id);
}
