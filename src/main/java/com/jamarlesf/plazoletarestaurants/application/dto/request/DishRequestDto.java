package com.jamarlesf.plazoletarestaurants.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishRequestDto {
    private String name;
    private String description;
    private Integer price;
    private String imageUrl;
    private Long categoryId;
    private Long restaurantId;
}
