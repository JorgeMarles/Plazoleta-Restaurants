package com.jamarlesf.plazoletarestaurants.infrastructure.security.adapter;

import com.jamarlesf.plazoletarestaurants.domain.spi.IUserContextPort;
import com.jamarlesf.plazoletarestaurants.infrastructure.security.utils.SecurityContextUtils;

public class UserContextAdapter implements IUserContextPort {
    @Override
    public String getAuthenticatedUserEmail() {
        return SecurityContextUtils.getAuthenticatedUserEmail();
    }
}
