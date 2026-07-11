package com.jamarlesf.plazoletarestaurants.domain.api;

import com.jamarlesf.plazoletarestaurants.domain.model.Order;

public interface IOrderServicePort {
    void save(Order order);
}
