package com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.adapter;

import com.jamarlesf.plazoletarestaurants.domain.spi.IOrderNotificationPort;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.client.INotificationFeignClient;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.dto.NotificationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class NotificationServiceFeignAdapter implements IOrderNotificationPort {

    private final INotificationFeignClient notificationFeignClient;

    @Override
    public void notifyOrderReady(String phoneNumber, String pin) {
        try {
            NotificationRequestDto request = new NotificationRequestDto("ORDER_READY", phoneNumber, pin);
            notificationFeignClient.sendNotification(request);
            log.info("Notificación de orden lista enviada correctamente al microservicio de notificaciones para el número {}", phoneNumber);
        } catch (Exception e) {
            log.error("Error comunicándose con el microservicio de notificaciones: {}", e.getMessage(), e);
        }
    }
}
