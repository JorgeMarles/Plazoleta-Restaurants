package com.jamarlesf.plazoletarestaurants.application.handler;

import com.jamarlesf.plazoletarestaurants.application.dto.request.DishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.request.UpdateDishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.DishResponseDto;

import com.jamarlesf.plazoletarestaurants.application.dto.request.UpdateDishStatusRequestDto;

import java.util.List;

public interface IDishHandler {
    void saveDish(DishRequestDto dishRequestDto, Long userId);
    List<DishResponseDto> getDishes();
    void updateDish(UpdateDishRequestDto updateDishRequestDto, Long userId);
    void updateDishStatus(Long id, UpdateDishStatusRequestDto updateDishStatusRequestDto, Long userId);
}
