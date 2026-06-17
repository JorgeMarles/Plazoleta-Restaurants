package com.jamarlesf.plazoletarestaurants.domain.spi;

import com.jamarlesf.plazoletarestaurants.domain.model.Dish;

import java.util.List;

public interface IDishPersistencePort {
    void save(Dish dish);
    List<Dish> findAll();
}
