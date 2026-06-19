package com.jamarlesf.plazoletarestaurants.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDishRequestDto {
    Long id;
    Integer price;
    String description;
}
