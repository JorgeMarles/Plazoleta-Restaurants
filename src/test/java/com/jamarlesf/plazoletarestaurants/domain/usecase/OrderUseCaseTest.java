package com.jamarlesf.plazoletarestaurants.domain.usecase;

import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderDish;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderStatus;
import com.jamarlesf.plazoletarestaurants.domain.spi.IDishPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IOrderPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IUserExternalPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IUserExternalPort userExternalPort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order orderRequest;
    private OrderDish orderDish1;
    private OrderDish orderDish2;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        orderDish1 = new OrderDish(1L, 2);
        orderDish2 = new OrderDish(2L, 1);

        orderRequest = new Order();
        orderRequest.setCustomerId(123L);
        orderRequest.setRestaurantId(10L);
        orderRequest.setDishes(Arrays.asList(orderDish1, orderDish2));
        
        restaurant = new Restaurant();
        restaurant.setId(10L);
    }

    @Test
    @DisplayName("Debe crear la orden con éxito y estado PENDIENTE cuando todo es válido")
    void shouldCreateOrderSuccessfully() {
        when(userExternalPort.isCustomer(123L)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(123L, OrderStatus.getInProcessStatuses())).thenReturn(false);
        
        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setRestaurant(restaurant);
        
        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setRestaurant(restaurant);
        
        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish1));
        when(dishPersistencePort.findById(2L)).thenReturn(Optional.of(dish2));

        assertDoesNotThrow(() -> orderUseCase.save(orderRequest));

        verify(userExternalPort).isCustomer(anyLong());
        verify(orderPersistencePort).hasActiveOrders(123L, OrderStatus.getInProcessStatuses());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).save(orderCaptor.capture());
        
        Order savedOrder = orderCaptor.getValue();
        assertEquals(OrderStatus.PENDING, savedOrder.getStatus(), "El estado debe ser PENDING");
        assertEquals(10L, savedOrder.getRestaurantId(), "El ID del restaurante debe ser el de los platos");
        assertNull(savedOrder.getChefId(), "El ID del chef debe ser nulo al crear el pedido");
    }

    @Test
    @DisplayName("Debe fallar si el usuario no tiene el rol de cliente")
    void shouldThrowExceptionWhenUserIsNotClient() {
        when(userExternalPort.isCustomer(123L)).thenReturn(false);

        assertThrows(DomainException.class, () -> orderUseCase.save(orderRequest));

        verify(userExternalPort).isCustomer(anyLong());
        verify(orderPersistencePort, never()).hasActiveOrders(anyLong(), anyList());

        verify(orderPersistencePort, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Debe fallar si el usuario ya tiene un pedido en proceso")
    void shouldThrowExceptionWhenUserHasActiveOrder() {
        when(userExternalPort.isCustomer(123L)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(123L, OrderStatus.getInProcessStatuses())).thenReturn(true);

        assertThrows(DomainException.class, () -> orderUseCase.save(orderRequest));

        verify(userExternalPort).isCustomer(anyLong());
        verify(orderPersistencePort).hasActiveOrders(123L, OrderStatus.getInProcessStatuses());

        verify(orderPersistencePort, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Debe fallar si alguna cantidad de los platos es menor o igual a cero")
    void shouldThrowExceptionWhenDishQuantityIsZeroOrLess() {
        when(userExternalPort.isCustomer(123L)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(123L, OrderStatus.getInProcessStatuses())).thenReturn(false);

        OrderDish invalidDish = new OrderDish(3L, 0); // Amount 0
        orderRequest.setDishes(Collections.singletonList(invalidDish));

        assertThrows(DomainException.class, () -> orderUseCase.save(orderRequest));

        verify(userExternalPort).isCustomer(anyLong());
        verify(orderPersistencePort).hasActiveOrders(123L, OrderStatus.getInProcessStatuses());

        verify(orderPersistencePort, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Debe fallar si los platos elegidos pertenecen a distintos restaurantes")
    void shouldThrowExceptionWhenDishesBelongToDifferentRestaurants() {
        when(userExternalPort.isCustomer(123L)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(123L, OrderStatus.getInProcessStatuses())).thenReturn(false);

        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setRestaurant(restaurant); 

        Dish dish2 = new Dish();
        dish2.setId(2L);
        Restaurant restaurant99 = new Restaurant();
        restaurant99.setId(99L);
        dish2.setRestaurant(restaurant99);

        when(dishPersistencePort.findById(1L)).thenReturn(Optional.of(dish1));
        when(dishPersistencePort.findById(2L)).thenReturn(Optional.of(dish2));

        assertThrows(DomainException.class, () -> orderUseCase.save(orderRequest));

        verify(userExternalPort).isCustomer(anyLong());
        verify(orderPersistencePort).hasActiveOrders(123L, OrderStatus.getInProcessStatuses());

        verify(orderPersistencePort, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Debe fallar si la lista de platos está vacía")
    void shouldThrowExceptionWhenDishListIsEmpty() {
        when(userExternalPort.isCustomer(123L)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(123L, OrderStatus.getInProcessStatuses())).thenReturn(false);

        orderRequest.setDishes(Collections.emptyList());

        assertThrows(DomainException.class, () -> orderUseCase.save(orderRequest));

        verify(userExternalPort).isCustomer(anyLong());
        verify(orderPersistencePort).hasActiveOrders(anyLong(), anyList());

        verify(orderPersistencePort, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Debe fallar si hay platos duplicados en el pedido")
    void shouldThrowExceptionWhenOrderHasDuplicateDishes() {
        when(userExternalPort.isCustomer(123L)).thenReturn(true);
        when(orderPersistencePort.hasActiveOrders(123L, OrderStatus.getInProcessStatuses())).thenReturn(false);

        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setRestaurant(restaurant);

        OrderDish duplicateDish1 = new OrderDish(1L, 2);
        OrderDish duplicateDish2 = new OrderDish(1L, 3);
        orderRequest.setDishes(Arrays.asList(duplicateDish1, duplicateDish2));

        DomainException e = assertThrows(DomainException.class, () -> orderUseCase.save(orderRequest));
        System.out.println(e.getMessage());

        verify(userExternalPort).isCustomer(anyLong());
        verify(orderPersistencePort).hasActiveOrders(anyLong(), anyList());

        verify(orderPersistencePort, never()).save(any(Order.class));
    }
}
