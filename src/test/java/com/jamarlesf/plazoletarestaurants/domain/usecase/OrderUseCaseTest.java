package com.jamarlesf.plazoletarestaurants.domain.usecase;

import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderDish;
import com.jamarlesf.plazoletarestaurants.domain.model.Restaurant;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderStatus;
import com.jamarlesf.plazoletarestaurants.domain.spi.IDishPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IOrderNotificationPort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IOrderPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IPinEncoderPort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IPinGeneratorPort;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IUserExternalPort userExternalPort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IOrderNotificationPort orderNotificationPort;

    @Mock
    private IPinEncoderPort pinEncoderPort;

    @Mock
    private IPinGeneratorPort pinGeneratorPort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order orderRequest;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        OrderDish orderDish1 = new OrderDish(1L, 2);
        OrderDish orderDish2 = new OrderDish(2L, 1);

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

    @Test
    @DisplayName("Debe marcar la orden como lista y notificar al cliente")
    void shouldMarkAsReadyAndNotifyCustomer() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setCustomerId(123L);
        order.setStatus(OrderStatus.IN_PREPARATION);

        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));
        when(pinGeneratorPort.generatePin()).thenReturn("123456");
        when(pinEncoderPort.encode("123456")).thenReturn("hashed_pin_123");
        when(userExternalPort.getCustomerPhone(123L)).thenReturn("+1234567890");

        assertDoesNotThrow(() -> orderUseCase.markAsReady(orderId));

        assertEquals(OrderStatus.READY, order.getStatus());
        assertEquals("hashed_pin_123", order.getPinHash());
        verify(orderPersistencePort).save(order);
        verify(orderNotificationPort).notifyOrderReady("+1234567890", "123456");
    }

    @Test
    @DisplayName("Debe marcar la orden como entregada cuando el pin es correcto y el estado es READY")
    void shouldMarkAsDeliveredWhenPinIsCorrect() {
        Long orderId = 1L;
        String rawPin = "123456";
        String hashedPin = "hashed_pin_123";

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.READY);
        order.setPinHash(hashedPin);

        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));
        when(pinEncoderPort.matches(rawPin, hashedPin)).thenReturn(true);

        assertDoesNotThrow(() -> orderUseCase.markAsDelivered(orderId, rawPin));

        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        verify(orderPersistencePort).save(order);
    }

    @Test
    @DisplayName("Debe fallar al marcar como entregado si el pin es incorrecto")
    void shouldThrowExceptionWhenPinIsIncorrectForDelivered() {
        Long orderId = 1L;
        String rawPin = "000000";
        String hashedPin = "hashed_pin_123";

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.READY);
        order.setPinHash(hashedPin);

        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));
        when(pinEncoderPort.matches(rawPin, hashedPin)).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class, () -> orderUseCase.markAsDelivered(orderId, rawPin));
        assertEquals("El pin ingresado es incorrecto", exception.getMessage());
        verify(orderPersistencePort, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Debe fallar al marcar como entregado si el estado no es READY")
    void shouldThrowExceptionWhenStateIsNotReadyForDelivered() {
        Long orderId = 1L;
        String rawPin = "123456";
        String hashedPin = "hashed_pin_123";

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setPinHash(hashedPin);

        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));
        when(pinEncoderPort.matches(rawPin, hashedPin)).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class, () -> orderUseCase.markAsDelivered(orderId, rawPin));
        assertEquals("El pedido debe estar en estado listo para poder ser entregado", exception.getMessage());
        verify(orderPersistencePort, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Debe cancelar la orden exitosamente si el estado es PENDING y pertenece al usuario")
    void shouldCancelOrderSuccessfully() {
        Long orderId = 1L;
        Long customerId = 123L;

        Order order = new Order();
        order.setId(orderId);
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.PENDING);

        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));

        assertDoesNotThrow(() -> orderUseCase.cancelOrder(orderId, customerId));

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderPersistencePort).save(order);
    }

    @Test
    @DisplayName("Debe fallar al cancelar la orden si no pertenece al usuario autenticado")
    void shouldThrowExceptionWhenOrderDoesNotBelongToUserOnCancel() {
        Long orderId = 1L;
        Long customerId = 123L;
        Long wrongCustomerId = 456L;

        Order order = new Order();
        order.setId(orderId);
        order.setCustomerId(wrongCustomerId);
        order.setStatus(OrderStatus.PENDING);

        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));

        DomainException exception = assertThrows(DomainException.class, () -> orderUseCase.cancelOrder(orderId, customerId));
        assertEquals("El pedido no pertenece al usuario autenticado", exception.getMessage());
        verify(orderPersistencePort, never()).save(any(Order.class));
    }
}
