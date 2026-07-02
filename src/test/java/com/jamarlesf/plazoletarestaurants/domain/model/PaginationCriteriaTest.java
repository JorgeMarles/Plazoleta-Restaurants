package com.jamarlesf.plazoletarestaurants.domain.model;

import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaginationCriteriaTest {

    @Test
    void shouldCreateWithDefaultValuesWhenNullsProvided() {
        PaginationCriteria criteria = new PaginationCriteria(null, null);

        assertEquals(0, criteria.getPageNumber());
        assertEquals(10, criteria.getPageSize());
    }

    @Test
    void shouldCreateWithProvidedValues() {
        PaginationCriteria criteria = new PaginationCriteria(2, 20);

        assertEquals(2, criteria.getPageNumber());
        assertEquals(20, criteria.getPageSize());
    }

    @Test
    void shouldThrowExceptionWhenPageNumberIsNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> new PaginationCriteria(-1, 10));

        assertEquals("El numero de pagina no puede ser negativo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPageSizeIsZero() {
        DomainException exception = assertThrows(DomainException.class, () -> new PaginationCriteria(0, 0));

        assertEquals("El tamaño de pagina debe ser mayor a cero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPageSizeIsNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> new PaginationCriteria(0, -5));

        assertEquals("El tamaño de pagina debe ser mayor a cero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPageSizeExceedsMaximum() {
        DomainException exception = assertThrows(DomainException.class, () -> new PaginationCriteria(0, 501));

        assertEquals("El tamaño de pagina no puede ser mayor a 500", exception.getMessage());
    }
}
