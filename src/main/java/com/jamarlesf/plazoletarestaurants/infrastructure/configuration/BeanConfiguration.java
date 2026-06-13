package com.jamarlesf.plazoletarestaurants.infrastructure.configuration;

import com.jamarlesf.plazoletarestaurants.domain.api.IRestaurantServicePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IRestaurantPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IUserExternalPort;
import com.jamarlesf.plazoletarestaurants.domain.usecase.RestaurantUseCase;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository.IRestaurantRepository;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.adapter.UserExternalAdapter;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.client.IUserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IUserFeignClient userFeignClient;

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IUserExternalPort userExternalPort() {
        return new UserExternalAdapter(userFeignClient);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort(), userExternalPort());
    }
}
