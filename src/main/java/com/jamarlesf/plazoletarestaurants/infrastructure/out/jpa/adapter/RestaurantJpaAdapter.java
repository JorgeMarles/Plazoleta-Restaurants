package com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.adapter;

import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import com.jamarlesf.plazoletarestaurants.domain.spi.IRestaurantPersistencePort;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.entity.RestaurantEntity;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void save(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntity(restaurant);
        restaurantRepository.save(restaurantEntity);
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantEntityMapper.toRestaurantList(restaurantRepository.findAll());
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(id);
        return restaurantEntity.map(restaurantEntityMapper::toRestaurant);
    }
}
