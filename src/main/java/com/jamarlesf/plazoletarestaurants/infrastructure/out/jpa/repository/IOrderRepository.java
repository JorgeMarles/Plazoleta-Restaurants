package com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.repository;

import com.jamarlesf.plazoletarestaurants.domain.model.OrderStatus;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByCustomerIdAndStatusIn(Long customerId, List<OrderStatus> statuses);
    Page<OrderEntity> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, Pageable pageable);
}
