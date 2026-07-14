package com.jamarlesf.plazoletarestaurants.domain.spi;

public interface IOrderNotificationPort {
    void notifyOrderReady(String phoneNumber, String pin);
}
