package com.jamarlesf.plazoletarestaurants.domain.api;

import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
    List<Restaurant> getRestaurants();
}
