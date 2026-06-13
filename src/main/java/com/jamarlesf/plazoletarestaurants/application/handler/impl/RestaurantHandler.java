package com.jamarlesf.plazoletarestaurants.application.handler.impl;

import com.jamarlesf.plazoletarestaurants.application.dto.request.RestaurantRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.RestaurantResponseDto;
import com.jamarlesf.plazoletarestaurants.application.handler.IRestaurantHandler;
import com.jamarlesf.plazoletarestaurants.application.mapper.IRestaurantRequestMapper;
import com.jamarlesf.plazoletarestaurants.application.mapper.IRestaurantResponseMapper;
import com.jamarlesf.plazoletarestaurants.domain.api.IRestaurantServicePort;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;

    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantRequestMapper.toRestaurant(restaurantRequestDto);
        restaurantServicePort.saveRestaurant(restaurant);
    }

    @Override
    public List<RestaurantResponseDto> getRestaurants() {
        return restaurantResponseMapper.toResponseList(restaurantServicePort.getRestaurants());
    }
}
