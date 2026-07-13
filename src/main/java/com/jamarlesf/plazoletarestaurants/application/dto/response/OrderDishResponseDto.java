package com.jamarlesf.plazoletarestaurants.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDishResponseDto {
    private DishResponseDto dish;
    private Integer amount;
}
