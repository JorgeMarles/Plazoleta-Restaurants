package com.jamarlesf.plazoletarestaurants.domain.model;

import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestaurantSortCriteriaTest {

    @ParameterizedTest
    @ValueSource(strings = {" ", "name"})
    void shouldCreateWithDefaultValueWhenNullProvided(String field) {
        RestaurantSortCriteria criteria = new RestaurantSortCriteria(field);

        assertEquals("name", criteria.sortField());
    }

    @Test
    void shouldCreateWithProvidedValueWhenValid() {
        RestaurantSortCriteria criteria = new RestaurantSortCriteria(null);

        assertEquals("name", criteria.sortField());
    }

    @Test
    void shouldThrowExceptionWhenInvalidFieldProvided() {
        DomainException exception = assertThrows(DomainException.class, () -> new RestaurantSortCriteria("id"));

        assertEquals("Campo de ordenamiento no permitido. Los campos permitidos son: [name]", exception.getMessage());
    }
}
