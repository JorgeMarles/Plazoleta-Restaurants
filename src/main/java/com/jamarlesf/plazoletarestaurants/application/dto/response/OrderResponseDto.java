package com.jamarlesf.plazoletarestaurants.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long id;
    private Long customerId;
    private LocalDateTime time;
    private String status;
    private Long chefId;
    private Long restaurantId;
    private List<OrderDishResponseDto> dishes;
}
