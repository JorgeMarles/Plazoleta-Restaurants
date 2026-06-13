package com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExternalDto {
    private Long id;
    private RoleExternalDto role;
}
