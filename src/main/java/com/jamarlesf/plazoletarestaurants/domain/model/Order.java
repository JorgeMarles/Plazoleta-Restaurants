package com.jamarlesf.plazoletarestaurants.domain.model;

import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Long customerId;
    private LocalDateTime time;
    private OrderStatus status;
    private Long chefId;
    private Long restaurantId;
    private List<OrderDish> dishes;
    private String pinHash;

    public void assignChefAndSetInPreparation(Long chefId) {
        if (this.status != OrderStatus.PENDING) {
            throw new DomainException("El pedido debe estar en estado pendiente para poder ser asignado");
        }
        setChefId(chefId);
        setStatus(OrderStatus.IN_PREPARATION);
    }

    public void markAsReady(String pinHash) {
        if (this.status != OrderStatus.IN_PREPARATION) {
            throw new DomainException("El pedido debe estar en preparación para poder ser marcado como listo");
        }
        setStatus(OrderStatus.READY);
        setPinHash(pinHash);
    }
}
