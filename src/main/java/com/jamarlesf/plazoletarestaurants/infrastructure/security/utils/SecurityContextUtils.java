package com.jamarlesf.plazoletarestaurants.infrastructure.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtils {

    private SecurityContextUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            if (auth.getDetails() instanceof Long userId) {
                return userId;
            } else if (auth.getDetails() instanceof Integer userIdInt) {
                return userIdInt.longValue();
            }
        }
        throw new IllegalStateException("No se pudo obtener el ID del usuario autenticado");
    }
}
