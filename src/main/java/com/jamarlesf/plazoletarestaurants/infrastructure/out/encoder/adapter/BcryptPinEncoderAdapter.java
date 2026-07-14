package com.jamarlesf.plazoletarestaurants.infrastructure.out.encoder.adapter;

import com.jamarlesf.plazoletarestaurants.domain.spi.IPinEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class BcryptPinEncoderAdapter implements IPinEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String pin) {
        return passwordEncoder.encode(pin);
    }

    @Override
    public boolean matches(String pin, String encodedPin) {
        return passwordEncoder.matches(pin, encodedPin);
    }
}
