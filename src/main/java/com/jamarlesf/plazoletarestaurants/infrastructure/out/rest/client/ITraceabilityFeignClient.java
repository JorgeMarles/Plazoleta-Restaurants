package com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.client;

import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.dto.TraceabilityLogRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "traceability-service", url = "${services.traceability.url}")
public interface ITraceabilityFeignClient {

    @PostMapping("/api/v1/logs/ready")
    void logReady(@RequestBody TraceabilityLogRequestDto dto);

    @PostMapping("/api/v1/logs/pending")
    void logPending(@RequestBody TraceabilityLogRequestDto dto);

    @PostMapping("/api/v1/logs/in-preparation")
    void logInPreparation(@RequestBody TraceabilityLogRequestDto dto);

    @PostMapping("/api/v1/logs/delivered")
    void logDelivered(@RequestBody TraceabilityLogRequestDto dto);

    @PostMapping("/api/v1/logs/cancelled")
    void logCancelled(@RequestBody TraceabilityLogRequestDto dto);
}
