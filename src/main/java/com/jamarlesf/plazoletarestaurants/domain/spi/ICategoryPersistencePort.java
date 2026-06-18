package com.jamarlesf.plazoletarestaurants.domain.spi;

import com.jamarlesf.plazoletarestaurants.domain.model.Category;

import java.util.Optional;

public interface ICategoryPersistencePort {
    Optional<Category> findById(Long id);
}
