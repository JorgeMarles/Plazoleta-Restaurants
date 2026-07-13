package com.jamarlesf.plazoletarestaurants.domain.model;

import com.jamarlesf.plazoletarestaurants.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void assignChefAndSetInPreparation_WithPendingStatus_ShouldAssignChefAndChangeStatus() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        Long chefId = 5L;

        // Act
        order.assignChefAndSetInPreparation(chefId);

        // Assert
        assertEquals(OrderStatus.IN_PREPARATION, order.getStatus());
        assertEquals(chefId, order.getChefId());
    }

    @Test
    void assignChefAndSetInPreparation_WithNonPendingStatus_ShouldThrowDomainException() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.IN_PREPARATION);
        Long chefId = 5L;

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> {
            order.assignChefAndSetInPreparation(chefId);
        });
        
        assertEquals("El pedido debe estar en estado pendiente para poder ser asignado", exception.getMessage());
    }
}
