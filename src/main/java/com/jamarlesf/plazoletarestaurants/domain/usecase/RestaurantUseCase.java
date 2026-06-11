package com.jamarlesf.plazoletarestaurants.domain.usecase;

import com.jamarlesf.plazoletarestaurants.domain.api.IRestaurantServicePort;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import com.jamarlesf.plazoletarestaurants.domain.spi.IRestaurantPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IUserExternalPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserExternalPort userExternalPort;

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Restaurant> getRestaurants() {
        return List.of();
    }
}
