package com.jamarlesf.plazoletarestaurants.domain.usecase;

import com.jamarlesf.plazoletarestaurants.domain.api.IOrderServicePort;
import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderDish;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderStatus;
import com.jamarlesf.plazoletarestaurants.domain.spi.IDishPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IOrderPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IUserExternalPort;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserExternalPort userExternalPort;
    private final IDishPersistencePort dishPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IUserExternalPort userExternalPort, IDishPersistencePort dishPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.userExternalPort = userExternalPort;
        this.dishPersistencePort = dishPersistencePort;
    }

    private void validateOrder(Order order) {
        if (!userExternalPort.isCustomer(order.getCustomerId())) {
            throw new DomainException("No es un cliente");
        }

        if (orderPersistencePort.hasActiveOrders(order.getCustomerId(), OrderStatus.getInProcessStatuses())) {
            throw new DomainException("No se puede realizar este pedido, tiene otros pedidos en proceso");
        }

        validateDishList(order.getDishes());
        validateNoDuplicateDishes(order.getDishes());
    }

    private void validateDishList(List<OrderDish> orderDishes) {
        if (orderDishes.isEmpty()) {
            throw new DomainException("El pedido debe contener platos");
        }
        for (OrderDish orderDish : orderDishes) {
            if (orderDish.getAmount() <= 0) {
                throw new DomainException("La cantidad de cada plato debe ser positiva");
            }
        }
    }

    private void validateNoDuplicateDishes(List<OrderDish> orderDishes) {
        Set<Long> dishIds = new HashSet<>();
        for (OrderDish orderDish : orderDishes) {
            if (!dishIds.add(orderDish.getDishId())) {
                throw new DomainException("El pedido no debe contener platos duplicados");
            }
        }
    }

    private Long validateAndGetRestaurantId(List<OrderDish> orderDishes) {
        Long restaurantId = null;
        for (OrderDish orderDish : orderDishes) {
            Dish dish = dishPersistencePort.findById(orderDish.getDishId())
                    .orElseThrow(() -> new DomainException("El plato con id " + orderDish.getDishId() + " no existe"));

            if (restaurantId == null) {
                restaurantId = dish.getRestaurant().getId();
            } else if (!dish.getRestaurant().getId().equals(restaurantId)) {
                throw new DomainException("Todos los platos deben pertenecer al mismo restaurante");
            }
        }
        return restaurantId;
    }

    @Override
    public void save(Order order) {
        validateOrder(order);
        
        Long restaurantId = validateAndGetRestaurantId(order.getDishes());
        
        order.setTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setRestaurantId(restaurantId);
        
        orderPersistencePort.save(order);
    }
}
