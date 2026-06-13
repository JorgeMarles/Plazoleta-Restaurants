package com.jamarlesf.plazoletarestaurants.application.handler;

import com.jamarlesf.plazoletarestaurants.application.dto.request.RestaurantRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.RestaurantResponseDto;

import java.util.List;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurant);
    List<RestaurantResponseDto> getRestaurants();
}
