package com.jamarlesf.plazoletarestaurants.infrastructure.out.generator.adapter;

import com.jamarlesf.plazoletarestaurants.domain.spi.IPinGeneratorPort;

import java.security.SecureRandom;

public class NumericPinGeneratorAdapter implements IPinGeneratorPort {

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generatePin() {
        return String.format("%06d", secureRandom.nextInt(1000000));
    }
}
