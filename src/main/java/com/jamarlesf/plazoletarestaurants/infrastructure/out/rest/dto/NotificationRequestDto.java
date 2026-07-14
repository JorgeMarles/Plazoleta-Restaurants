package com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationRequestDto {
    private String type;
    private String phone;
    private String pin;
}
