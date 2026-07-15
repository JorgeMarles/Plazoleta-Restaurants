package com.jamarlesf.plazoletarestaurants.domain.spi;

public interface ITraceabilityExternalPort {
    void logPendingOrder(Long orderId, Long customerId, String customerEmail);
    void logInPreparationOrder(Long orderId, Long employeeId, String employeeEmail);
    void logReadyOrder(Long orderId);
    void logDeliveredOrder(Long orderId);
    void logCancelledOrder(Long orderId);
}
