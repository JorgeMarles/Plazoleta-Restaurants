package com.jamarlesf.plazoletarestaurants.domain.spi;

import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantPersistencePort {
    void save(Restaurant restaurant);
    List<Restaurant> findAll();
}
