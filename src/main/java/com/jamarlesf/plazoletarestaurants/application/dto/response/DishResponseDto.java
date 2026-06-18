package com.jamarlesf.plazoletarestaurants.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishResponseDto {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private String imageUrl;
    private Boolean active;
    private CategoryResponseDto category;
    private RestaurantResponseDto restaurant;
}
