package com.jamarlesf.plazoletarestaurants.domain.usecase;

import com.jamarlesf.plazoletarestaurants.domain.api.IRestaurantServicePort;
import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import com.jamarlesf.plazoletarestaurants.domain.spi.IRestaurantPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IUserExternalPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserExternalPort userExternalPort;

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        validateRestaurantFields(restaurant);
        if(!userExternalPort.isOwner(restaurant.getOwnerId())) {
            throw new DomainException("El usuario especificado no es un propietario");
        }
        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public List<Restaurant> getRestaurants() {
        return restaurantPersistencePort.getRestaurants();
    }

    private void validateRestaurantFields(Restaurant restaurant) {
        validateName(restaurant.getName());
        validatePhoneNumber(restaurant.getPhone());
        validateNit(restaurant.getNit());
    }

    private void validateName(String name) {
        if(!name.matches("^(?=.*[A-Za-záéíóúÁÉÍÓÚñÑ]).+$")) {
            throw new DomainException("El nombre no contiene letras");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches("^\\+?\\d+$")) {
            throw new DomainException("Solo se permiten dígitos y +");
        }
        if (phoneNumber.replaceAll("\\d", "").length() > 13) {
            throw new DomainException("El teléfono no puede exceder 13 dígitos");
        }
    }

    private void validateNit(String nit) {
        if(!nit.matches("^\\d+$")) {
            throw new DomainException("El nit solo puede contener números");
        }
    }

}
