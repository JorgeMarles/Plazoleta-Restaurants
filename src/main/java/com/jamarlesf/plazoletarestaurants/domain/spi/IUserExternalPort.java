package com.jamarlesf.plazoletarestaurants.domain.spi;

public interface IUserExternalPort {
    boolean isOwner(Long userId);
    boolean isCustomer(Long userId);
}
