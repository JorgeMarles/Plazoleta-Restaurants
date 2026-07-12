package com.jamarlesf.plazoletarestaurants.domain.model;

public enum OrderStatus {
    PENDING,
    IN_PREPARATION,
    CANCELLED,
    READY,
    DELIVERED;

    public static java.util.List<OrderStatus> getInProcessStatuses() {
        return java.util.Arrays.asList(PENDING, IN_PREPARATION, READY);
    }
}
