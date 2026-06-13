package com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.client;

import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.dto.UserExternalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service", url = "${services.users.url}", path = "/api/v1")
public interface IUserFeignClient {

    @GetMapping("/users/{userId}")
    UserExternalDto getUserById(@PathVariable("userId") Long userId);
}
