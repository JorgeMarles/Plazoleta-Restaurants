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
        DomainException exception = assertThrows(DomainException.class, () -> order.assignChefAndSetInPreparation(chefId));
        
        assertEquals("El pedido debe estar en estado pendiente para poder ser asignado", exception.getMessage());
    }

    @Test
    void markAsReady_WithInPreparationStatus_ShouldChangeStatusAndSetPinHash() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.IN_PREPARATION);
        String pinHash = "hashed_pin_123";

        // Act
        order.markAsReady(pinHash);

        // Assert
        assertEquals(OrderStatus.READY, order.getStatus());
        assertEquals(pinHash, order.getPinHash());
    }

    @Test
    void markAsReady_WithNonInPreparationStatus_ShouldThrowDomainException() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        String pinHash = "hashed_pin_123";

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> order.markAsReady(pinHash));
        
        assertEquals("El pedido debe estar en preparación para poder ser marcado como listo", exception.getMessage());
    }

    @Test
    void markAsCancelled_WithPendingStatus_ShouldChangeStatusToCancelled() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);

        // Act
        order.markAsCancelled();

        // Assert
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void markAsCancelled_WithNonPendingStatus_ShouldThrowDomainException() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.IN_PREPARATION);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, order::markAsCancelled);
        
        assertEquals("Lo sentimos, tu pedido no puede ser cancelado", exception.getMessage());
    }
}
