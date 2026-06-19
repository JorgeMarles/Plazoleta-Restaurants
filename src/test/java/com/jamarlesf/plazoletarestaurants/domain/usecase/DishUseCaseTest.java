package com.jamarlesf.plazoletarestaurants.domain.usecase;

import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import com.jamarlesf.plazoletarestaurants.domain.model.Category;
import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import com.jamarlesf.plazoletarestaurants.domain.spi.ICategoryPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IDishPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Dish Use Case Tests")
class DishUseCaseTest {

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Dish dish;
    private Restaurant restaurant;
    private Category category;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurant 123");
        restaurant.setAddress("Address 123");
        restaurant.setPhone("+573333333333");
        restaurant.setOwnerId(1L);
        restaurant.setLogoUrl("https://logo.com");
        restaurant.setNit("123456789");

        category = new Category();
        category.setName("Category 123");
        category.setDescription("Description Category 123");
        category.setId(1L);

        dish = new Dish();
        dish.setName("Dish 123");
        dish.setCategory(category);
        dish.setRestaurant(restaurant);
        dish.setDescription("Description Dish 123");
        dish.setImageUrl("https://image.com");
        dish.setPrice(9500);
    }

    @ParameterizedTest
    @CsvSource({
            "Plato 1, Descripcion, https://image.com, 9500",
            "Plato, Descripcion, https://image.com, 1",
            "Carne Asada, a, imagen, 2147483647"
    })
    void save_WhenValidDish_ShouldSaveSuccessfully(String name, String description, String imageUrl, Integer price) {
        doNothing().when(dishPersistencePort).save(any());
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryPersistencePort.findById(1L)).thenReturn(Optional.of(category));
        dish.setName(name);
        dish.setDescription(description);
        dish.setImageUrl(imageUrl);
        dish.setPrice(price);

        assertDoesNotThrow(() -> dishUseCase.save(dish));

        verify(dishPersistencePort).save(any(Dish.class));
        verify(categoryPersistencePort).findById(dish.getCategory().getId());
        verify(restaurantPersistencePort).findById(dish.getRestaurant().getId());
        assertTrue(dish.getActive());
    }

    @Test
    void save_WhenRestaurantNotFound_ShouldThrowDomainException() {
        Restaurant falseRestaurant = new Restaurant();
        falseRestaurant.setId(2L);
        dish.setRestaurant(falseRestaurant);
        assertThrows(DomainException.class, () -> dishUseCase.save(dish));
        verify(restaurantPersistencePort).findById(2L);
    }

    @Test
    void save_WhenCategoryNotFound_ShouldThrowDomainException() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(Optional.of(restaurant));

        Category falseCategory = new Category();
        falseCategory.setId(2L);
        dish.setCategory(falseCategory);
        assertThrows(DomainException.class, () -> dishUseCase.save(dish));
        verify(categoryPersistencePort).findById(2L);
        verify(restaurantPersistencePort).findById(1L);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            -1,
            0,
            -2147483648,
    })
    void save_WhenPriceIsNotPositive_ShouldThrowDomainException(Integer price) {
        dish.setPrice(price);

        assertThrows(DomainException.class, () -> dishUseCase.save(dish));
    }

    @Test
    void update_WhenDishNotFound_ShouldThrowDomainException() {
        assertThrows(DomainException.class, () -> dishUseCase.updateDish(1L, 1, "new Description"));

        verify(dishPersistencePort).findById(1L);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            -1,
            0,
            -2147483648,
    })
    void update_WhenPriceIsNotPositive_ShouldThrowDomainException(Integer price) {
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish));

        assertThrows(DomainException.class, () -> dishUseCase.updateDish(1L, price, "new Description"));

        verify(dishPersistencePort).findById(1L);
    }
}
