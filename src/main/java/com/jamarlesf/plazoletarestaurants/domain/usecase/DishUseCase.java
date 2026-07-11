package com.jamarlesf.plazoletarestaurants.domain.usecase;

import com.jamarlesf.plazoletarestaurants.domain.api.IDishServicePort;
import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import com.jamarlesf.plazoletarestaurants.domain.model.Category;
import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import com.jamarlesf.plazoletarestaurants.domain.spi.ICategoryPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IDishPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IRestaurantPersistencePort;

import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;

import java.util.List;
public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, ICategoryPersistencePort categoryPersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    private void validatePrice(Integer price) {
        if (price <= 0) {
            throw new DomainException("El precio debe ser positivo mayor a 0");
        }
    }

    @Override
    public void save(Dish dish, Long requestUserId) {
        validatePrice(dish.getPrice());

        Restaurant restaurant = restaurantPersistencePort
                .findById(dish.getRestaurant().getId())
                .orElseThrow(() -> new DomainException("El restaurante especificado no existe"));

        if(!requestUserId.equals(restaurant.getOwnerId())) {
            throw new DomainException("No eres el propietario de este restaurante");
        }

        dish.setRestaurant(restaurant);

        Category category = categoryPersistencePort
                .findById(dish.getCategory().getId())
                .orElseThrow(() -> new DomainException("La categoria especificada no existe"));
        dish.setCategory(category);

        dish.setActive(true);
        dishPersistencePort.save(dish);
    }

    @Override
    public List<Dish> findAll() {
        return dishPersistencePort.findAll();
    }

    @Override
    public void updateDish(Long id, Integer price, String description, Long requestUserId) {
        Dish dish = dishPersistencePort.findById(id).orElseThrow(() -> new DomainException("El plato con id " + id + " no existe"));

        if(!requestUserId.equals(dish.getRestaurant().getOwnerId())) {
            throw new DomainException("No eres el propietario de este restaurante");
        }

        validatePrice(price);
        dish.setPrice(price);
        dish.setDescription(description);
        dishPersistencePort.save(dish);
    }

    @Override
    public void updateDishStatus(Long id, Boolean active, Long requestUserId) {
        Dish dish = dishPersistencePort.findById(id).orElseThrow(() -> new DomainException("El plato con id " + id + " no existe"));
        if(!requestUserId.equals(dish.getRestaurant().getOwnerId())) {
            throw new DomainException("No eres el propietario de este restaurante");
        }

        dish.setActive(active);
        dishPersistencePort.save(dish);
    }

    @Override
    public PageModel<Dish> findByRestaurantId(Long restaurantId, Long categoryId, com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria paginationCriteria) {
        return dishPersistencePort.findByRestaurantId(restaurantId, categoryId, paginationCriteria);
    }
}
