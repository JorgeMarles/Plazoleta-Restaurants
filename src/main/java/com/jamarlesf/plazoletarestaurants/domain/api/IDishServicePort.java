package com.jamarlesf.plazoletarestaurants.domain.api;

import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;

import java.util.List;

public interface IDishServicePort {
    void save(Dish dish, Long requestUserId);
    List<Dish> findAll();
    void updateDish(Long id, Integer price, String description, Long requestUserId);
    void updateDishStatus(Long id, Boolean active, Long requestUserId);
    PageModel<Dish> findByRestaurantId(Long restaurantId, Long categoryId, PaginationCriteria paginationCriteria);
}
