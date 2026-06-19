package com.jamarlesf.plazoletarestaurants.application.handler;

import com.jamarlesf.plazoletarestaurants.application.dto.request.DishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.request.UpdateDishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.DishResponseDto;

import java.util.List;

public interface IDishHandler {
    void saveDish(DishRequestDto dishRequestDto);
    List<DishResponseDto> getDishes();
    void updateDish(UpdateDishRequestDto updateDishRequestDto);
}
