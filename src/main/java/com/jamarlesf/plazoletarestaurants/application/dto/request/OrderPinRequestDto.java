package com.jamarlesf.plazoletarestaurants.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPinRequestDto {
    @NotBlank(message = "El pin no puede estar vacío")
    private String pin;
}
