package com.jamarlesf.plazoletarestaurants.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private List<OrderDishRequestDto> dishes;
}
