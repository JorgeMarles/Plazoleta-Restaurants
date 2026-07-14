package com.jamarlesf.plazoletarestaurants.domain.spi;

public interface IPinEncoderPort {
    String encode(String pin);
    boolean matches(String pin, String encodedPin);
}
