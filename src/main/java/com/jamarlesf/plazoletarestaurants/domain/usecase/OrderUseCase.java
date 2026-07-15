package com.jamarlesf.plazoletarestaurants.domain.usecase;

import com.jamarlesf.plazoletarestaurants.domain.api.IOrderServicePort;
import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import com.jamarlesf.plazoletarestaurants.domain.model.Dish;
import com.jamarlesf.plazoletarestaurants.domain.model.Order;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderDish;
import com.jamarlesf.plazoletarestaurants.domain.model.OrderStatus;
import com.jamarlesf.plazoletarestaurants.domain.spi.IDishPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IOrderNotificationPort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IOrderPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IPinEncoderPort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IPinGeneratorPort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IUserExternalPort;
import com.jamarlesf.plazoletarestaurants.domain.spi.ITraceabilityExternalPort;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.jamarlesf.plazoletarestaurants.domain.model.PageModel;
import com.jamarlesf.plazoletarestaurants.domain.model.PaginationCriteria;
public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserExternalPort userExternalPort;
    private final IDishPersistencePort dishPersistencePort;
    private final IOrderNotificationPort orderNotificationPort;
    private final IPinEncoderPort pinEncoderPort;
    private final IPinGeneratorPort pinGeneratorPort;
    private final ITraceabilityExternalPort traceabilityLogPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IUserExternalPort userExternalPort, IDishPersistencePort dishPersistencePort, IOrderNotificationPort orderNotificationPort, IPinEncoderPort pinEncoderPort, IPinGeneratorPort pinGeneratorPort, ITraceabilityExternalPort traceabilityLogPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.userExternalPort = userExternalPort;
        this.dishPersistencePort = dishPersistencePort;
        this.orderNotificationPort = orderNotificationPort;
        this.pinEncoderPort = pinEncoderPort;
        this.pinGeneratorPort = pinGeneratorPort;
        this.traceabilityLogPort = traceabilityLogPort;
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
    public void save(Order order, String userEmail) {
        validateOrder(order);
        
        Long restaurantId = validateAndGetRestaurantId(order.getDishes());
        
        order.setTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setRestaurantId(restaurantId);
        
        orderPersistencePort.save(order);
        
        traceabilityLogPort.logPendingOrder(order.getId(), order.getCustomerId(), userEmail);
    }

    @Override
    public PageModel<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, PaginationCriteria paginationCriteria) {
        return orderPersistencePort.findByRestaurantIdAndStatus(restaurantId, status, paginationCriteria);
    }

    @Override
    public void assignOrder(Long orderId, Long employeeId, String employeeEmail) {
        if (!userExternalPort.isEmployee(employeeId)) {
            throw new DomainException("El usuario no es un empleado");
        }
        Order order = getOrderById(orderId);
        order.assignChefAndSetInPreparation(employeeId);
        orderPersistencePort.save(order);
        
        traceabilityLogPort.logInPreparationOrder(order.getId(), employeeId, employeeEmail);
    }

    private Order getOrderById(Long orderId) {
        return orderPersistencePort.findById(orderId)
                .orElseThrow(() -> new DomainException("El pedido con id " + orderId + " no existe"));
    }

    @Override
    public void markAsReady(Long orderId) {
        Order order = getOrderById(orderId);

        String pin = pinGeneratorPort.generatePin();

        order.markAsReady(pinEncoderPort.encode(pin));
        orderPersistencePort.save(order);

        String phone = userExternalPort.getCustomerPhone(order.getCustomerId());
        orderNotificationPort.notifyOrderReady(phone, pin);
        
        traceabilityLogPort.logReadyOrder(order.getId());
    }

    @Override
    public void markAsDelivered(Long orderId, String pin) {
        Order order = getOrderById(orderId);

        if (!pinEncoderPort.matches(pin, order.getPinHash())) {
            throw new DomainException("El pin ingresado es incorrecto");
        }

        order.markAsDelivered();
        orderPersistencePort.save(order);
        
        traceabilityLogPort.logDeliveredOrder(order.getId());
    }

    @Override
    public void cancelOrder(Long orderId, Long customerId) {
        Order order = getOrderById(orderId);
        
        if (!order.getCustomerId().equals(customerId)) {
            throw new DomainException("El pedido no pertenece al usuario autenticado");
        }
        
        order.markAsCancelled();
        orderPersistencePort.save(order);
        
        traceabilityLogPort.logCancelledOrder(order.getId());
    }
}
