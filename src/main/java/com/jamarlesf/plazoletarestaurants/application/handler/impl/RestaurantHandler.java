package com.jamarlesf.plazoletarestaurants.application.handler.impl;

import com.jamarlesf.plazoletarestaurants.application.dto.request.RestaurantRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.PageResponseDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.RestaurantBasicResponseDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.RestaurantResponseDto;
import com.jamarlesf.plazoletarestaurants.application.handler.IRestaurantHandler;
import com.jamarlesf.plazoletarestaurants.application.mapper.IRestaurantRequestMapper;
import com.jamarlesf.plazoletarestaurants.application.mapper.IRestaurantResponseMapper;
import com.jamarlesf.plazoletarestaurants.domain.api.IRestaurantServicePort;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import com.jamarlesf.plazoletarestaurants.domain.model.RestaurantSortCriteria;
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
        restaurantServicePort.save(restaurant);
    }

    @Override
    public List<RestaurantResponseDto> getRestaurants() {
        return restaurantResponseMapper.toResponseList(restaurantServicePort.findAll());
    }

    @Override
    public PageResponseDto<RestaurantBasicResponseDto> getRestaurantsPaginated(Integer page, Integer size) {
        PaginationCriteria pagination = new PaginationCriteria(page, size);
        RestaurantSortCriteria sort = new RestaurantSortCriteria("name");
        return restaurantResponseMapper.toPageResponseDto(restaurantServicePort.getRestaurantsPaginatedAndSorted(pagination, sort));
    }
}
