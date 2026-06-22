package com.jamarlesf.plazoletarestaurants.application.handler.impl;

import com.jamarlesf.plazoletarestaurants.application.dto.request.DishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.request.UpdateDishRequestDto;
import com.jamarlesf.plazoletarestaurants.application.dto.response.DishResponseDto;
import com.jamarlesf.plazoletarestaurants.application.handler.IDishHandler;
import com.jamarlesf.plazoletarestaurants.application.mapper.IDishRequestMapper;
import com.jamarlesf.plazoletarestaurants.application.mapper.IDishResponseMapper;
import com.jamarlesf.plazoletarestaurants.domain.api.IDishServicePort;
import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
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
}
