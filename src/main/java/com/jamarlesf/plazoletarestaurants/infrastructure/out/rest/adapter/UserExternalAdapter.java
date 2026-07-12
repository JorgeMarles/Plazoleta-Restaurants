package com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.adapter;

import com.jamarlesf.plazoletarestaurants.domain.spi.IUserExternalPort;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.exception.UserExternalServiceException;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.client.IUserFeignClient;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.dto.UserExternalDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class UserExternalAdapter implements IUserExternalPort {

    private static final String OWNER_ROLE_NAME = "PROPIETARIO";
    private static final String CUSTOMER_ROLE_NAME = "CLIENTE";

    private final IUserFeignClient userFeignClient;

    public boolean isRole(Long userId, String roleName) {
        try {
            UserExternalDto user = userFeignClient.getUserById(userId);
            return user != null
                    && user.getRole() != null
                    && roleName.equals(user.getRole().getName());
        } catch (Exception e) {
            log.error("Error communicating with user service", e);
            throw new UserExternalServiceException("Error communicating with the external user service");
        }
    }

    @Override
    public boolean isOwner(Long userId) {
        return isRole(userId, OWNER_ROLE_NAME);
    }

    @Override
    public boolean isCustomer(Long userId) {
        return isRole(userId, CUSTOMER_ROLE_NAME);
    }
}
