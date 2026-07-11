package com.jamarlesf.plazoletarestaurants.application.handler.impl;

import com.jamarlesf.plazoletarestaurants.application.dto.request.DishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.request.UpdateDishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.request.UpdateDishStatusRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.DishResponseDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.PageResponseDto;
import com.jamarlesf.plazoletarestaurants.application.handler.IDishHandler;
import com.jamarlesf.plazoletarestaurants.application.mapper.IDishRequestMapper;
import com.jamarlesf.plazoletarestaurants.application.mapper.IDishResponseMapper;
import com.jamarlesf.plazoletarestaurants.domain.api.IDishServicePort;
import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public void saveDish(DishRequestDto dishRequestDto, Long userId) {
        Dish dish = dishRequestMapper.toDish(dishRequestDto);
        dishServicePort.save(dish, userId);
    }

    @Override
    public List<DishResponseDto> getDishes() {
        return dishResponseMapper.toResponseList(dishServicePort.findAll());
    }

    @Override
    public void updateDish(UpdateDishRequestDto updateDishRequestDto, Long userId) {
        dishServicePort.updateDish(
                updateDishRequestDto.getId(),
                updateDishRequestDto.getPrice(),
                updateDishRequestDto.getDescription(),
                userId
        );
    }

    @Override
    public void updateDishStatus(Long id, UpdateDishStatusRequestDto updateDishStatusRequestDto, Long userId) {
        dishServicePort.updateDishStatus(
                id,
                updateDishStatusRequestDto.getActive(),
                userId
        );
    }
    @Override
    public PageResponseDto<DishResponseDto> getDishesByRestaurant(Long restaurantId, Long categoryId, Integer page, Integer size) {
        PaginationCriteria paginationCriteria = new PaginationCriteria(page, size);
        return dishResponseMapper.toPageResponseDto(dishServicePort.findByRestaurantId(restaurantId, categoryId, paginationCriteria));
    }
}
