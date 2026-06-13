package com.jamarlesf.plazoletarestaurants;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PlazoletarestaurantsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlazoletarestaurantsApplication.class, args);
    }
}
