package com.jamarlesf.plazoletarestaurants.infrastructure.out.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraceabilityLogRequestDto {
    private Long orderId;
    private Long customerId;
    private String customerEmail;
    private Long employeeId;
    private String employeeEmail;
    private LocalDateTime dateTime;
}
