package com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.client;

import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.dto.NotificationRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notifications-service", url = "${services.notifications.url}", path = "/api/v1")
public interface INotificationFeignClient {

    @PostMapping("/notifications")
    void sendNotification(@RequestBody NotificationRequestDto notificationRequestDto);
}
