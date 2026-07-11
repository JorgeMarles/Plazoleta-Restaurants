package com.jamarlesf.plazoletarestaurants.domain.spi;

import com.jamarlesf.plazoletarestaurants.domain.model.Dish;

import java.util.List;
import java.util.Optional;
import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;

public interface IDishPersistencePort {
    void save(Dish dish);
    List<Dish> findAll();
    Optional<Dish> findById(Long id);
    PageModel<Dish> findByRestaurantId(Long restaurantId, Long categoryId, PaginationCriteria paginationCriteria);
}
