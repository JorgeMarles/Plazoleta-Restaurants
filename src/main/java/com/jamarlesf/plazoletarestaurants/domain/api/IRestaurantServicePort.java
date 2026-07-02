package com.jamarlesf.plazoletarestaurants.domain.api;

import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import com.jamarlesf.plazoletarestaurants.domain.model.RestaurantSortCriteria;

import java.util.List;

public interface IRestaurantServicePort {
    void save(Restaurant restaurant);
    List<Restaurant> findAll();
    PageModel<Restaurant> getRestaurantsPaginatedAndSorted(PaginationCriteria pagination, RestaurantSortCriteria sortCriteria);
}
