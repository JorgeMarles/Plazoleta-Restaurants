package com.jamarlesf.plazoletarestaurants.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDish {
    private Long dishId;
    private Integer amount;
}
