package com.jamarlesf.plazoletarestaurants.infrastructure.input.rest;

import com.jamarlesf.plazoletarestaurants.application.dto.request.OrderRequestDto;
import com.jamarlesf.plazoletarestaurants.application.handler.IOrderHandler;
import com.jamarlesf.plazoletarestaurants.infrastructure.security.utils.SecurityContextUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders/")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @Operation(summary = "Create a new order", description = "Creates a new order with the provided dishes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order data")
    })
    @PostMapping()
    @PreAuthorize("hasAuthority('CLIENTE')")
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        orderHandler.saveOrder(orderRequestDto, SecurityContextUtils.getAuthenticatedUserId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Assign an order", description = "Assigns an order to the authenticated employee and changes its status to IN_PREPARATION")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order state")
    })
    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    public ResponseEntity<Void> assignOrder(@PathVariable Long id) {
        Long employeeId = SecurityContextUtils.getAuthenticatedUserId();
        orderHandler.assignOrder(id, employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Mark order as ready", description = "Marks an order as ready and notifies the customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order marked as ready"),
            @ApiResponse(responseCode = "400", description = "Invalid order state")
    })
    @PatchMapping("/{id}/ready")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    public ResponseEntity<Void> markAsReady(@PathVariable Long id) {
        orderHandler.markAsReady(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
