package com.jamarlesf.plazoletarestaurants.domain.model;

import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;

import java.util.List;

public record RestaurantSortCriteria(String sortField) {

    private static final List<String> ALLOWED_FIELDS = List.of("name");

    public RestaurantSortCriteria(String sortField) {
        this.sortField = (sortField != null && !sortField.trim().isEmpty()) ? sortField : "name";
        validate();
    }

    private void validate() {
        if (!ALLOWED_FIELDS.contains(this.sortField)) {
            throw new DomainException("Campo de ordenamiento no permitido. Los campos permitidos son: " + ALLOWED_FIELDS);
        }
    }

}
