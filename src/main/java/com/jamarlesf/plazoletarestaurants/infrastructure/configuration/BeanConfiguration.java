package com.jamarlesf.plazoletarestaurants.infrastructure.configuration;

import com.jamarlesf.plazoletarestaurants.domain.api.IDishServicePort;
import com.jamarlesf.plazoletarestaurants.domain.api.IRestaurantServicePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.ICategoryPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IDishPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IRestaurantPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IUserExternalPort;
import com.jamarlesf.plazoletarestaurants.domain.usecase.DishUseCase;
import com.jamarlesf.plazoletarestaurants.domain.usecase.RestaurantUseCase;
import com.jamarlesf.plazoletarestaurants.domain.api.IOrderServicePort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IOrderPersistencePort;
import com.jamarlesf.plazoletarestaurants.domain.usecase.OrderUseCase;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.adapter.OrderJpaAdapter;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository.IOrderRepository;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.adapter.CategoryJpaAdapter;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.adapter.DishJpaAdapter;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper.ICategoryEntityMapper;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository.ICategoryRepository;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository.IDishRepository;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository.IRestaurantRepository;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.adapter.UserExternalAdapter;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.client.IUserFeignClient;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.adapter.NotificationServiceFeignAdapter;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.client.INotificationFeignClient;
import com.jamarlesf.plazoletarestaurants.domain.spi.IOrderNotificationPort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IPinEncoderPort;
import com.jamarlesf.plazoletarestaurants.domain.spi.IPinGeneratorPort;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.encoder.adapter.BcryptPinEncoderAdapter;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.generator.adapter.NumericPinGeneratorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IUserFeignClient userFeignClient;
    private final INotificationFeignClient notificationFeignClient;

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;
    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

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

    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishJpaAdapter(dishRepository, dishEntityMapper);
    }

    @Bean
    public ICategoryPersistencePort categoryPersistencePort() {
        return new CategoryJpaAdapter(categoryRepository, categoryEntityMapper);
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort(), categoryPersistencePort(), restaurantPersistencePort());
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort() {
        return new OrderJpaAdapter(orderRepository, orderEntityMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IOrderNotificationPort orderNotificationPort() {
        return new NotificationServiceFeignAdapter(notificationFeignClient);
    }

    @Bean
    public IPinEncoderPort pinEncoderPort(PasswordEncoder passwordEncoder) {
        return new BcryptPinEncoderAdapter(passwordEncoder);
    }

    @Bean
    public IPinGeneratorPort pinGeneratorPort() {
        return new NumericPinGeneratorAdapter();
    }

    @Bean
    public IOrderServicePort orderServicePort() {
        return new OrderUseCase(orderPersistencePort(), userExternalPort(), dishPersistencePort(), orderNotificationPort(), pinEncoderPort(passwordEncoder()), pinGeneratorPort());
    }
}
