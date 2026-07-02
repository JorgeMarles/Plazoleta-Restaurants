package com.jamarlesf.plazoletarestaurants.domain.usecase;

import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import com.jamarlesf.plazoletarestaurants.domain.spi.IRestaurantPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IUserExternalPort;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Restaurant Use Case Tests")
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserExternalPort userExternalPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setName("Restaurant 123");
        restaurant.setAddress("Address 123");
        restaurant.setPhone("+573333333333");
        restaurant.setOwnerId(1L);
        restaurant.setLogoUrl("https://logo.com");
        restaurant.setNit("123456789");
    }

    @ParameterizedTest
    //name, address, ownerId, phone, logoUrl, nit
    @CsvSource({
            "Restaurant 1, Address 1234, 1, 3284772423, https://image.com, 372638363",
            "Restaurant 2, Address 1234, 1, +3284772423, https://image.com, 372638363",
            "Restaurant 3, Address 1234, 1, 1234567890123, https://image.com, 372638363",
            "Restaurant ABC, Address 1234, 1, +123456789012, https://image.com, 372638363"
    })
    void save_whenValidRestaurant_ShouldSaveSuccesfully(String name, String address, Long ownerId, String phone, String logoUrl, String nit) {
        doNothing().when(restaurantPersistencePort).save(any());
        when(userExternalPort.isOwner(1L)).thenReturn(true);

        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setOwnerId(ownerId);
        restaurant.setPhone(phone);
        restaurant.setLogoUrl(logoUrl);
        restaurant.setNit(nit);

        assertDoesNotThrow(() -> restaurantUseCase.save(restaurant));

        verify(restaurantPersistencePort).save(any(Restaurant.class));
        verify(userExternalPort).isOwner(1L);
    }

    //Nombre puede contener números, pero no se permiten nombres con solo números
    @ParameterizedTest
    @ValueSource(strings = {
            "12456",
            "1",
            "1 1"
    })
    void save_Restaurant_whenInvalidName_ShouldThrowException(String name) {
        restaurant.setName(name);

        assertThrows(DomainException.class, () -> restaurantUseCase.save(restaurant));

        verify(restaurantPersistencePort, times(0)).save(any(Restaurant.class));
        verify(userExternalPort, times(0)).isOwner(1L);
    }

    //Teléfono debe ser únicamente numérico, de máximo 13 caracteres y puede comenzar con +
    @ParameterizedTest
    @ValueSource(strings = {
            "12345678901234",
            "+12345678901234",
            "abc",
            "11112212+",
            "111++1221",
            "++11256",
            "152212a21",
            "111  2335"
    })
    void save_whenInvalidPhone_ShouldThrowException(String phone) {
        restaurant.setPhone(phone);

        assertThrows(DomainException.class, () -> restaurantUseCase.save(restaurant));

        verify(restaurantPersistencePort, times(0)).save(any(Restaurant.class));
        verify(userExternalPort, times(0)).isOwner(1L);
    }

    //Nit debe ser únicamente numérico
    @ParameterizedTest
    @ValueSource(strings = {
            "acb123",
            "abc"
    })
    void save_Restaurant_whenInvalidNit_ShouldThrowException(String nit) {
        restaurant.setNit(nit);

        assertThrows(DomainException.class, () -> restaurantUseCase.save(restaurant));

        verify(restaurantPersistencePort, times(0)).save(any(Restaurant.class));
        verify(userExternalPort, times(0)).isOwner(1L);
    }

    //ownerId debe ser el Id de un usuario con rol propietario
    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    void save_whenInvalidOwnerId_ShouldThrowException(Long ownerId) {
        when(userExternalPort.isOwner(anyLong())).thenReturn(false);
        restaurant.setOwnerId(ownerId);

        DomainException exception = assertThrows(DomainException.class, () -> restaurantUseCase.save(restaurant));
        assertTrue(exception.getMessage().contains("propietario"));

        verify(restaurantPersistencePort, times(0)).save(any(Restaurant.class));
        verify(userExternalPort, times(1)).isOwner(anyLong());
    }

    @Test
    void findAllExist_ShouldReturnRestaurantList() {
        List<Restaurant> expectedRestaurants = List.of(
                new Restaurant(1L, "Restaurant 1", "Address 1", 10L, "+573111111111", "https://logo-1.com", "123456789"),
                new Restaurant(2L, "Restaurant 2", "Address 2", 20L, "+573222222222", "https://logo-2.com", "987654321")
        );

        when(restaurantPersistencePort.findAll()).thenReturn(expectedRestaurants);

        List<Restaurant> actualRestaurants = restaurantUseCase.findAll();

        assertSame(expectedRestaurants, actualRestaurants);
        verify(restaurantPersistencePort, times(1)).findAll();
    }

    @Test
    void findAllExist_ShouldReturnEmptyList() {
        when(restaurantPersistencePort.findAll()).thenReturn(List.of());

        List<Restaurant> actualRestaurants = restaurantUseCase.findAll();

        assertTrue(actualRestaurants.isEmpty());
        verify(restaurantPersistencePort, times(1)).findAll();
    }

    @Test
    void getRestaurantsPaginatedAndSorted_ShouldReturnPageModel() {
        com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria pagination = new com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria(0, 10);
        com.jamarlesf.plazoletarestaurants.domain.model.RestaurantSortCriteria sort = new com.jamarlesf.plazoletarestaurants.domain.model.RestaurantSortCriteria("name");
        com.jamarlesf.plazoletarestaurants.domain.model.PageModel<Restaurant> expectedPage = new com.jamarlesf.plazoletarestaurants.domain.model.PageModel<>(List.of(restaurant), 0, 10, 1, 1, true, true);

        when(restaurantPersistencePort.findAllPaginated(pagination, sort)).thenReturn(expectedPage);

        com.jamarlesf.plazoletarestaurants.domain.model.PageModel<Restaurant> actualPage = restaurantUseCase.getRestaurantsPaginatedAndSorted(pagination, sort);

        assertSame(expectedPage, actualPage);
        verify(restaurantPersistencePort, times(1)).findAllPaginated(pagination, sort);
    }


}
