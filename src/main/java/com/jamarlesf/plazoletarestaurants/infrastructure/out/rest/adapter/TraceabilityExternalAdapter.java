package com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.adapter;

import com.jamarlesf.plazoletarestaurants.domain.spi.ITraceabilityExternalPort;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.client.ITraceabilityFeignClient;
import com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.dto.TraceabilityLogRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
public class TraceabilityExternalAdapter implements ITraceabilityExternalPort {

    private final ITraceabilityFeignClient feignClient;

    @Override
    public void logPendingOrder(Long orderId, Long customerId, String customerEmail) {
        try {
            feignClient.logPending(TraceabilityLogRequestDto.builder()
                    .orderId(orderId)
                    .customerId(customerId)
                    .customerEmail(customerEmail)
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("Failed to send pending order log to traceability service for order {}", orderId, e);
        }
    }

    @Override
    public void logInPreparationOrder(Long orderId, Long employeeId, String employeeEmail) {
        try {
            feignClient.logInPreparation(TraceabilityLogRequestDto.builder()
                    .orderId(orderId)
                    .employeeId(employeeId)
                    .employeeEmail(employeeEmail)
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("Failed to send in-preparation order log to traceability service for order {}", orderId, e);
        }
    }

    @Override
    public void logReadyOrder(Long orderId) {
        try {
            feignClient.logReady(TraceabilityLogRequestDto.builder()
                    .orderId(orderId)
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("Failed to send ready order log to traceability service for order {}", orderId, e);
        }
    }

    @Override
    public void logDeliveredOrder(Long orderId) {
        try {
            feignClient.logDelivered(TraceabilityLogRequestDto.builder()
                    .orderId(orderId)
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("Failed to send delivered order log to traceability service for order {}", orderId, e);
        }
    }

    @Override
    public void logCancelledOrder(Long orderId) {
        try {
            feignClient.logCancelled(TraceabilityLogRequestDto.builder()
                    .orderId(orderId)
                    .dateTime(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("Failed to send cancelled order log to traceability service for order {}", orderId, e);
        }
    }
}
