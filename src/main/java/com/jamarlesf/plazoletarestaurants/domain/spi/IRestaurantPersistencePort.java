package com.jamarlesf.plazoletarestaurants.domain.spi;

import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import com.jamarlesf.plazoletarestaurants.domain.model.RestaurantSortCriteria;

import java.util.List;
import java.util.Optional;

public interface IRestaurantPersistencePort {
    void save(Restaurant restaurant);
    List<Restaurant> findAll();
    Optional<Restaurant> findById(Long id);
    PageModel<Restaurant> findAllPaginated(PaginationCriteria pagination, RestaurantSortCriteria sortCriteria);
}
