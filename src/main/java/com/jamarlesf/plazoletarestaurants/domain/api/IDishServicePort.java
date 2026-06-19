package com.jamarlesf.plazoletarestaurants.domain.api;

import com.jamarlesf.plazoletarestaurants.domain.model.Dish;

import java.util.List;

public interface IDishServicePort {
    void save(Dish dish);
    List<Dish> findAll();
    void updateDish(Long id, Integer price, String description);
}
